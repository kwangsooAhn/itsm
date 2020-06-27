package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfUserTask(
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

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.tokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(super.setTokenData(completedToken))
        return completedToken
    }
}
