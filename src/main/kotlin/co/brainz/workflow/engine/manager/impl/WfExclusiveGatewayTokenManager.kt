package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService

class WfExclusiveGatewayTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        token.assigneeId = wfTokenDto.assigneeId
        val saveToken = wfTokenManagerService.saveToken(token)

        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType
        //GW Element도 데이터를 저장
        saveToken.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        wfTokenManagerService.saveNotification(saveToken)

        return wfTokenDto
    }
}
