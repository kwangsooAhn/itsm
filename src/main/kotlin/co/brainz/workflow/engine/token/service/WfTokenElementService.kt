package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.folder.service.WfFolderService
import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
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
class WfTokenElementService(
    private val wfTokenActionService: WfTokenActionService,
    private val wfActionService: WfActionService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfFolderService: WfFolderService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Init Token.
     *
     * @param wfTokenDto
     */
    fun initToken(wfTokenDto: WfTokenDto) {
        val documentDto = wfTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val instanceDto = documentDto?.let { WfInstanceDto(instanceId = "", document = it) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }
        instance?.let { wfFolderService.createFolder(instance) }

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(wfTokenDto.documentId!!)
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process!!.processId)
        wfTokenDto.elementType = startElement.elementType
        wfTokenDto.elementId = startElement.elementId
        when (startElement.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
        }
        val startToken = wfTokenActionService.createToken(instance!!, wfTokenDto)
        wfTokenDto.tokenId = startToken.tokenId
        setTokenAction(wfTokenDto)
    }

    /**
     * Token Gate (Action).
     *
     * @param wfTokenDto
     */
    fun setTokenAction(wfTokenDto: WfTokenDto) {
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        val wfElementEntity = wfActionService.getElement(wfTokenEntity.element.elementId)
        logger.debug("Token Element Type : {}", wfElementEntity.elementType)
        when (wfElementEntity.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> setCommonAction(
                wfTokenEntity,
                wfElementEntity,
                wfTokenDto
            )
            WfElementConstants.ElementType.USER_TASK.value -> setUserTaskAction(
                wfTokenEntity,
                wfElementEntity,
                wfTokenDto
            )
        }
    }

    private fun setCommonAction(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        wfTokenDto: WfTokenDto
    ) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, wfTokenDto)
            }
        }
    }

    /**
     * Action - UserTask.
     *
     * @param wfTokenEntity
     * @param wfElementEntity
     * @param wfTokenDto
     */
    private fun setUserTaskAction(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        wfTokenDto: WfTokenDto
    ) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> {
                wfTokenDto.assigneeId = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            }
            WfElementConstants.Action.REJECT.value -> {
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.REJECT_ID.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.setReject(wfTokenEntity, wfTokenDto, values)
            }
            WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.setWithdraw(wfTokenEntity, wfTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, wfTokenDto)
            }
        }
    }

    /**
     * Set Next Token Save.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     * @return WfTokenEntity
     */
    private fun setNextTokenSave(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto): WfTokenEntity {
        val saveTokenEntity = wfTokenRepository.save(wfTokenEntity)

        //Token Data
        val dataList: MutableList<WfTokenDataEntity> = mutableListOf()
        wfTokenDto.data?.forEach {
            dataList.add(
                WfTokenDataEntity(
                    tokenId = saveTokenEntity.tokenId,
                    componentId = it.componentId,
                    value = it.value
                )
            )
        }
        wfTokenDataRepository.saveAll(dataList)

        return saveTokenEntity
    }

    /**
     * Set Next Token Entity.
     *
     * @param nextElementEntity
     * @param wfTokenEntity
     * @return WfTokenEntity
     */
    private fun setNextTokenEntity(nextElementEntity: WfElementEntity, wfTokenEntity: WfTokenEntity): WfTokenEntity {
        val nextTokenEntity = WfTokenEntity(
            tokenId = "",
            element = nextElementEntity,
            tokenStatus = WfTokenConstants.Status.RUNNING.code,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfTokenEntity.instance
        )
        when (nextElementEntity.elementType) {
            WfElementConstants.ElementType.USER_TASK.value -> {
                nextTokenEntity.assigneeId = getAttributeValue(
                    nextElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
                nextTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                nextTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
                nextTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                nextTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }
        return nextTokenEntity
    }

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    private fun goToNext(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {
        wfTokenDto.elementId = wfElementEntity.elementId
        val nextElementEntity = wfElementService.getNextElement(wfTokenDto)

        when (nextElementEntity.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                wfTokenRepository.save(newTokenEntity)
                wfInstanceService.completeInstance(wfTokenEntity.instance.instanceId)
                if (!wfTokenEntity.instance.pTokenId.isNullOrEmpty()) {
                    val pTokenId = wfTokenEntity.instance.pTokenId!!
                    val pTokenEntity = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
                    pTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                    pTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                    val savePTokenEntity = wfTokenRepository.save(pTokenEntity)
                    val newElementEntity = wfActionService.getElement(savePTokenEntity.element.elementId)
                    //TODO: 문서의 양식이 다르기 때문에 데이터가 다르다. wfTokenDto 값을 현재 문서에 맞게 갱신하는 작업 필요 (mapping-id)
                    goToNext(savePTokenEntity, newElementEntity, wfTokenDto)
                }
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                setNextTokenSave(newTokenEntity, wfTokenDto)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, wfTokenDto)
                wfTokenDto.tokenId = saveTokenEntity.tokenId
                val newElementEntity = wfActionService.getElement(saveTokenEntity.element.elementId)
                goToNext(saveTokenEntity, newElementEntity, wfTokenDto)
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                //TODO: SubProcess 로 시작시 초기 데이터를 갱신해야한다. 데이터 구조가 다름. (wfTokenDto 를 mapping-id로 처리)
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, wfTokenDto)

                //New Instance
                val documentId = getAttributeValue(
                    nextElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
                )
                val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
                val wfInstanceDto = WfInstanceDto(
                    instanceId = "",
                    document = wfDocumentEntity,
                    instanceStatus = WfInstanceConstants.Status.RUNNING.code,
                    pTokenId = saveTokenEntity.tokenId
                )
                val wfInstanceEntity = wfInstanceService.createInstance(wfInstanceDto)
                wfFolderService.addInstance(originInstance = wfTokenEntity.instance, addedInstance = wfInstanceEntity)

                //Call Document Start Element
                val startElement = wfElementService.getStartElement(wfDocumentEntity.process!!.processId)
                wfTokenDto.elementType = startElement.elementType
                wfTokenDto.elementId = startElement.elementId
                when (startElement.elementType) {
                    WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                        wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
                    }
                }
                val startToken = wfTokenActionService.createToken(wfInstanceEntity, wfTokenDto)
                wfTokenDto.tokenId = startToken.tokenId
                goToNext(startToken, startElement, wfTokenDto)
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
