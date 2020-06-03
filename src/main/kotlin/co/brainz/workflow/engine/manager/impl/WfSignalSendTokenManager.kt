package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSignalSendTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    lateinit var assigneeId: String

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        token.assigneeId = wfTokenDto.assigneeId
        val saveToken = wfTokenManagerService.saveToken(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType
        saveToken.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        val token = wfTokenManagerService.getToken(wfTokenDto.tokenId)
        val targetDocumentIds = mutableListOf<String>()
        token.element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                targetDocumentIds.add(it.attributeValue)
            }
        }
        val makeDocumentTokens =
            wfTokenManagerService.makeRestTemplateTokenDto(token, targetDocumentIds)
        makeDocumentTokens.forEach {
            it.assigneeId = wfTokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return wfTokenDto
    }
}
