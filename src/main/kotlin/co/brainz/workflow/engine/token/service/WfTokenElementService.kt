package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
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
                            private val wfInstanceService: WfInstanceService,
                            private val wfElementService: WfElementService,
                            private val wfTokenDataRepository: WfTokenDataRepository,
                            private val wfDocumentRepository: WfDocumentRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Init Start.
     *
     * @param wfTokenDto
     * @param wfDocumentEntity
     * @param instance
     */
    fun initStart(wfTokenDto: WfTokenDto, wfDocumentEntity: WfDocumentEntity, instance: WfInstanceEntity) {
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
        val wfTokenEntity= setCommonStartEvent(wfTokenDto, startElement, instance)
        goToNext(wfTokenEntity, wfTokenDto)
        val nextElement = getNextElement(wfTokenEntity)
        val currentTokenEntity = wfTokenRepository.findWfTokenEntityByInstanceAndElementId(instance, nextElement.elementId)
        wfTokenDto.tokenId = currentTokenEntity.tokenId
    }

    /**
     * CommonStartEvent.
     *
     * @param wfTokenDto
     * @param wfElementEntity
     * @param wfInstanceEntity
     * @return WfTokenEntity
     */
    fun setCommonStartEvent(wfTokenDto: WfTokenDto, wfElementEntity: WfElementEntity, wfInstanceEntity: WfInstanceEntity): WfTokenEntity {
        logger.debug("Token Action : {}", wfTokenDto.action)

        wfTokenDto.elementId = wfElementEntity.elementId
        wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
        return wfTokenActionService.createToken(wfInstanceEntity, wfTokenDto)
    }

    /**
     * Get Next Element.
     *
     * @param wfTokenEntity
     * @return WfElementEntity
     */
    fun getNextElement(wfTokenEntity: WfTokenEntity): WfElementEntity {
        val arrow = wfActionService.getArrowElements(wfTokenEntity.elementId)[0]
        val elementId = wfActionService.getNextElementId(arrow)
        return wfActionService.getElement(elementId)
    }

    /**
     * UserTask.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun setUserTask(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            WfElementConstants.Action.REJECT.value -> {
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(wfElementEntity.elementDataEntities, WfElementConstants.AttributeId.REJECT_ID.value)
                values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] = getAttributeValue(wfElementEntity.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
                values[WfElementConstants.AttributeId.ASSIGNEE.value] = getAttributeValue(wfElementEntity.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
                wfTokenActionService.setReject(wfTokenEntity, wfTokenDto, values)
            }
            WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.setWithdraw(wfTokenEntity, wfTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)
                goToNext(wfTokenEntity, wfTokenDto)
            }
        }
    }

    /**
     * CommonEndEvent.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun setCommonEndEvent(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        wfTokenRepository.save(wfTokenEntity)
    }

    /**
     * SubProcess.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun setSubProcess(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)

        //New Instance
        val wfInstanceDto = WfInstanceDto(
                instanceId = "",
                document = wfTokenEntity.instance.document,
                instanceStatus = WfInstanceConstants.Status.RUNNING.code,
                callTokenId = wfTokenDto.tokenId
        )
        val wfInstanceEntity = wfInstanceService.createInstance(wfInstanceDto)

        //Call Document Start Element
        val documentId = getAttributeValue(wfElementEntity.elementDataEntities, WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value)
        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
        val startTokenEntity = WfTokenEntity(
                tokenId = "",
                tokenStatus = WfTokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                instance = wfInstanceEntity,
                elementId = startElement.elementId
        )
        wfTokenRepository.save(startTokenEntity)

        //Call Document Running Element
        val arrows = wfActionService.getArrowElements(startElement.elementId)[0]
        val nextElementId = wfActionService.getNextElementId(arrows)
        val nextElement = wfActionService.getElement(nextElementId)

        //New Token
        val newTokenEntity = WfTokenEntity(
                tokenId = "",
                tokenStatus = WfTokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                assigneeType = getAttributeValue(nextElement.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value),
                assigneeId = getAttributeValue(nextElement.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value),
                instance = wfInstanceEntity,
                elementId = nextElementId
        )
        wfTokenRepository.save(newTokenEntity)
    }

    /**
     * Exclusive Gateway.
     *
     * @param wfTokenEntity,
     * @param wfTokenDto
     */
    fun setExclusiveGateway(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
    }

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    private fun goToNext(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
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
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                val newTokenEntity = WfTokenEntity(
                        tokenId = "",
                        elementId = element.elementId,
                        tokenStatus = WfTokenConstants.Status.FINISH.code,
                        tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                        tokenEndDt = LocalDateTime.now(ZoneId.of("UTC")),
                        instance = wfTokenEntity.instance
                )
                setCommonEndEvent(newTokenEntity, wfTokenDto)
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
