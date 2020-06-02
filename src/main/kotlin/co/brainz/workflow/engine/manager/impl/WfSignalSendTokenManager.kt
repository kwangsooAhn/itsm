package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfSignalSendTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    lateinit var assigneeId: String

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenManagerService = constructorManager.getTokenManagerService()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()
    private val wfTokenMappingValue = constructorManager.getTokenMappingValue()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        token.assigneeId = wfTokenDto.assigneeId
        val saveToken = wfTokenRepository.save(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType
        saveToken.tokenData = wfTokenDataRepository.saveAll(super.setTokenData(wfTokenDto))

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        val token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        val targetDocumentIds = mutableListOf<String>()
        token.element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                targetDocumentIds.add(it.attributeValue)
            }
        }
        val makeDocumentTokens =
            wfTokenMappingValue.makeRestTemplateTokenDto(token, targetDocumentIds)
        makeDocumentTokens.forEach {
            it.assigneeId = wfTokenDto.assigneeId
            WfEngine(constructorManager).startWorkflow(it)
        }

        return wfTokenDto
    }
}
