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

        // MappingId를 찾아 넘어갈 데이터를 선정하고 프로세스를 시작한다.
        val elementInfo = wfTokenManagerService.getElement(tokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                elementInfo.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = tokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenManagerService.makeRestTemplateTokenDto(super.createTokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = tokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return tokenDto
    }

}
