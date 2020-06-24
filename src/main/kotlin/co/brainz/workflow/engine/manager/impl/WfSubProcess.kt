package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSubProcess(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = false
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        super.tokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
        super.setCandidate(super.tokenEntity)
        super.tokenEntity.assigneeId?.let {
            createTokenDto.assigneeId = it
        }

        // Set mapping component data.
        val element = wfTokenManagerService.getElement(createTokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                element.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = createTokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.tokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = createTokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        super.suspendToken(createTokenDto)

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.tokenEntity.assigneeId =
            wfTokenManagerService.getCurrentAssigneeForChildProcess(completedToken.tokenId) ?: completedToken.assigneeId
        wfTokenManagerService.saveToken(super.tokenEntity)
        return completedToken
    }
}
