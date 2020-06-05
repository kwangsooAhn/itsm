package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfCommonStartEventTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {
    override fun createElementToken(wfTokenDto: WfTokenDto): WfTokenDto {
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
