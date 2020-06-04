package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSubProcessTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val tokenDto = super.createToken(wfTokenDto)
        super.createTokenEntity.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(tokenDto))
        super.setCandidate(super.createTokenEntity)

        // Set mapping component data.
        val element = wfTokenManagerService.getElement(tokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                element.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = tokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.createTokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = tokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return tokenDto
    }
}
