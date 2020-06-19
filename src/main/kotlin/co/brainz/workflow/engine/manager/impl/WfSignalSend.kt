package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfSignalSend(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        super.createTokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
        super.setCandidate(super.createTokenEntity)
        super.createTokenEntity.assigneeId?.let {
            createTokenDto.assigneeId = it
        }

        // 시그널 이벤트의 document 를 생성
        val targetDocumentIds = mutableListOf<String>()
        super.createTokenEntity.element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                targetDocumentIds.add(it.attributeValue)
            }
        }
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.createTokenEntity, targetDocumentIds)
        makeDocumentTokens.forEach {
            it.assigneeId = createTokenDto.assigneeId
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        createNextTokenDto.isAutoComplete = super.setAutoComplete(createNextTokenDto.elementType)
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.createTokenEntity.assigneeId =
            wfTokenManagerService.getCurrentAssigneeForChildProcess(completedToken.tokenId) ?: completedToken.assigneeId
        wfTokenManagerService.saveToken(super.createTokenEntity)
        return completedToken
    }
}
