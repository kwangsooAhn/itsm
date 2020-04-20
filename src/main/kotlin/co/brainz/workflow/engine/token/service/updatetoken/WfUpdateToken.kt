package co.brainz.workflow.engine.token.service.updatetoken

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.WfTokenActionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneId

abstract class WfUpdateToken(
    protected val wfTokenActionService: WfTokenActionService,
    protected val wfActionService: WfActionService,
    protected val wfTokenRepository: WfTokenRepository,
    protected val wfInstanceService: WfInstanceService,
    protected val wfElementService: WfElementService,
    protected val wfTokenDataRepository: WfTokenDataRepository,
    protected val wfDocumentRepository: WfDocumentRepository
) {
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 토큰을 업데이트한다.
     */
    abstract fun updateToken(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto)

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    protected fun goToNext(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {
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

                //Call Document Start Element
                val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
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
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    protected fun getAttributeValue(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }
}
