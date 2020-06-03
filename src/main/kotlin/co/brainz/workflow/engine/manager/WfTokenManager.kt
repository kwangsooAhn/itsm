package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import java.time.LocalDateTime
import java.time.ZoneId

abstract class WfTokenManager(val constructorManager: ConstructorManager) {

    private val wfElementService = constructorManager.getElementService()
    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenManagerService = constructorManager.getTokenManagerService()

    open fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        val saveToken = wfTokenRepository.save(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType
        return wfTokenDto
    }

    open fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val element = wfElementService.getNextElement(wfTokenDto)
        wfTokenDto.elementId = element.elementId
        wfTokenDto.elementType = element.elementType
        wfTokenDto.isAutoComplete = when (element.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value,
            WfElementConstants.ElementType.MANUAL_TASK.value,
            WfElementConstants.ElementType.SIGNAL_SEND.value,
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> true
            else -> false
        }
        val tokenManager = WfTokenManagerFactory(constructorManager).getTokenManager(wfTokenDto.elementType)
        return tokenManager.createToken(wfTokenDto)
    }

    open fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        wfTokenRepository.save(token)
        wfTokenDto.tokenId = token.tokenId
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
