package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfManualTask(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        super.createTokenEntity.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
        super.setCandidate(super.createTokenEntity)

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        return completedToken
    }
}
