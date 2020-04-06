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
     * @param wfTokenDto
     */
    fun userTask(wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            else -> {
                when (wfTokenDto.action) {
                    WfElementConstants.Action.REGIST.value -> wfTokenActionService.registration(wfTokenEntity, wfTokenDto)
                    WfElementConstants.Action.REJECT.value -> wfTokenActionService.reject(wfTokenEntity, wfTokenDto)
                    WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.withdraw(wfTokenEntity, wfTokenDto)
                }
                goToNext(wfTokenEntity)
            }
        }
    }

    /**
     * EndEvent.
     *
     * @param wfTokenDto
     */
    fun endEvent(wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)

    }

    /**
     * SubProcess.
     *
     * @param wfTokenDto
     */
    fun subProcess(wfTokenDto: WfTokenDto) {
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
