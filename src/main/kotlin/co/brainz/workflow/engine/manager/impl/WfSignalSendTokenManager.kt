package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSignalSendTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createTokenEntity.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        super.setCandidate(super.createTokenEntity)

        return wfTokenDto
    }

    override fun createNextElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(wfTokenDto.elementType)
            .createToken(wfTokenDto)
    }

    override fun completeElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val targetDocumentIds = mutableListOf<String>()
        super.createTokenEntity.element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                targetDocumentIds.add(it.attributeValue)
            }
        }
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.createTokenEntity, targetDocumentIds)
        makeDocumentTokens.forEach {
            it.assigneeId = wfTokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return wfTokenDto
    }
}
