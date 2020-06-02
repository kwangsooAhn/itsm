package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

abstract class WfTokenManager(val constructorManager: ConstructorManager) {

    lateinit var wfTokenEntity: WfTokenEntity


    private val wfElementService = constructorManager.getElementService()
    private val wfElementRepository = constructorManager.getElementRepository()
    private val wfInstanceRepository = constructorManager.getInstanceRepository()
    private val wfTokenRepository = constructorManager.getTokenRepository()

    open fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
        this.wfTokenEntity = wfTokenRepository.save(token)
        wfTokenDto.tokenId = this.wfTokenEntity.tokenId
        wfTokenDto.elementId = this.wfTokenEntity.element.elementId
        wfTokenDto.elementType = this.wfTokenEntity.element.elementType
        return wfTokenDto
    }

    open fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val element = wfElementService.getNextElement(wfTokenDto)
        wfTokenDto.elementId = element.elementId
        wfTokenDto.elementType = element.elementType
        wfTokenDto.isAutoComplete = when (element.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value,
            WfElementConstants.ElementType.MANUAL_TASK.value -> true
            else -> false
        }
        val tokenManager = WfTokenManagerFactory(constructorManager).getTokenManager(wfTokenDto.elementType)
        return tokenManager.createToken(wfTokenDto)
    }

    open fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        this.wfTokenEntity = wfTokenRepository.save(token)
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            wfTokenDto.parentTokenId = token.instance.pTokenId
        }
        return wfTokenDto
    }

    /**
     * Set Token Data.
     *
     * @param wfTokenDto
     * @return MutableList<WfTokenDataEntity>
     */
    fun setTokenData(wfTokenDto: WfTokenDto): MutableList<WfTokenDataEntity> {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        if (wfTokenDto.data != null) {
            for (tokenDataDto in wfTokenDto.data!!) {
                val tokenDataEntity = WfTokenDataEntity(
                    tokenId = wfTokenDto.tokenId,
                    componentId = tokenDataDto.componentId,
                    value = tokenDataDto.value
                )
                tokenDataEntities.add(tokenDataEntity)
            }
        }
        return tokenDataEntities
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    fun getAttributeValue(
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

    /**
     * Get AttributeValues.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return MutableList<String> (attributeValue)
     */
    fun getAttributeValues(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): MutableList<String> {
        val attributeValues: MutableList<String> = mutableListOf()
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValues.add(data.attributeValue)
            }
        }
        return attributeValues
    }
}
