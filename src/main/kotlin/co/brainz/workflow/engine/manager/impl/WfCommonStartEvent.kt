package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfCommonStartEvent(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {
    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        createNextTokenDto.isAutoComplete = super.setAutoComplete(createNextTokenDto.elementType)
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        return completedToken
    }

}
