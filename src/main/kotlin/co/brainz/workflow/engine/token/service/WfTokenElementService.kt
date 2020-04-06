package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfTokenElementService(private val wfTokenActionService: WfTokenActionService,
                            private val wfActionService: WfActionService,
                            private val wfTokenRepository: WfTokenRepository,
                            private val wfDocumentRepository: WfDocumentRepository,
                            private val wfInstanceService: WfInstanceService,
                            private val wfElementService: WfElementService,
                            private val wfTokenDataRepository: WfTokenDataRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Init Element.
     *
     * @param wfTokenDto
     */
    fun initStart(wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)

        //instance 생성
        val documentDto = wfTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val processId = documentDto?.process?.processId
        val instanceDto = documentDto?.let { WfInstanceDto(instanceId = "", document = it) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }

        val startElementId = processId?.let { wfElementService.getStartElement(it).elementId }.toString()
        val arrow = wfActionService.getArrowElements(startElementId)[0]
        val initElementId = wfActionService.getNextElementId(arrow) //신청서작성단계 elementId
        val initElement = wfActionService.getElement(initElementId)

        wfTokenDto.assigneeType = getAttributeValue(initElement.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
        wfTokenDto.assigneeId = getAttributeValue(initElement.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
        wfTokenDto.elementId = initElementId
        when (wfTokenDto.action) {
            WfElementConstants.Action.REGIST.value -> {
                wfTokenActionService.initRegistration(wfTokenDto, instance)
                goToNext(wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get())
            }
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.initSave(wfTokenDto, instance)
        }
    }

    /**
     * UserTask.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun userTask(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            WfElementConstants.Action.REJECT.value -> {
                val element = wfActionService.getElement(wfTokenEntity.elementId)
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.REJECT_ID.value)
                values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
                values[WfElementConstants.AttributeId.ASSIGNEE.value] = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
                wfTokenActionService.reject(wfTokenEntity, wfTokenDto, values)
            }
            else -> {
                when (wfTokenDto.action) {
                    WfElementConstants.Action.REGIST.value -> wfTokenActionService.registration(wfTokenEntity, wfTokenDto)
                    WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.withdraw(wfTokenEntity, wfTokenDto)
                }
                goToNext(wfTokenEntity)
            }
        }
    }

    /**
     * EndEvent.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun endEvent(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)

    }

    /**
     * SubProcess.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun subProcess(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)

    }

    /**
     * Exclusive Gateway.
     *
     * @param wfTokenEntity,
     * @param wfTokenDto
     */
    fun exclusiveGateway(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
    }

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     */
    private fun goToNext(wfTokenEntity: WfTokenEntity) {
        val arrows = wfActionService.getArrowElements(wfTokenEntity.elementId)
        //TODO: GateWay 면 Element 선택 추가.
        val nextElementId = wfActionService.getNextElementId(arrows[0])
        val element = wfActionService.getElement(nextElementId)
        when (element.elementType) {
            WfElementConstants.ElementType.USER_TASK.value -> {
                val assignee = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
                val assigneeType = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
                val newTokenEntity = WfTokenEntity(
                        tokenId = "",
                        elementId = element.elementId,
                        tokenStatus = WfTokenConstants.Status.RUNNING.code,
                        tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                        assigneeId = assignee,
                        assigneeType = assigneeType,
                        instance = wfTokenEntity.instance
                )
                val saveTokenEntity = wfTokenRepository.save(newTokenEntity)

                //Token Data
                val tokenDataList = wfTokenDataRepository.findTokenDataEntityByTokenId(wfTokenEntity.tokenId)
                val dataList: MutableList<WfTokenDataEntity> = mutableListOf()
                tokenDataList.forEach { tokenData ->
                    dataList.add(WfTokenDataEntity(tokenId = saveTokenEntity.tokenId, componentId = tokenData.componentId, value = tokenData.value))
                }
                wfTokenDataRepository.saveAll(dataList)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                val conditionField = getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.CONDITION_ITEM.value)
                when (conditionField == WfElementConstants.AttributeValue.ACTION.value) {
                    true -> {
                        //TODO: 나가는 화살표 조건들과 비교해서 같은 action 경로 선택
                    }
                    false -> {
                        //TODO: 현재 Element에서 나가는 화살표의 action 값 비교 후 Element 선택
                    }
                }
            }
            WfElementConstants.ElementType.END_EVENT.value -> {
                wfInstanceService.completeInstance(wfTokenEntity.instance.instanceId)
                //TODO: #8321 종료이벤트 로직 강화
                //호출한 부모 토큰이 존재할 경우 해당 토큰 호출
            }
        }
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    private fun getAttributeValue(elementDataEntities: MutableList<WfElementDataEntity>, attributeId: String): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }

}
