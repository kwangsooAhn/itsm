package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSubProcessTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createTokenEntity.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        super.setCandidate(super.createTokenEntity)

        // Set mapping component data.
        val element = wfTokenManagerService.getElement(wfTokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                element.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = wfTokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.createTokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = wfTokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return wfTokenDto
    }

    override fun createNextElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(wfTokenDto.elementType)
            .createToken(wfTokenDto)
    }

    override fun completeElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        return wfTokenDto
    }
}
