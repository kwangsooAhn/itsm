package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import java.time.LocalDateTime
import java.time.ZoneId

class WfUserTaskTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val tokenDto = super.createToken(wfTokenDto)
        super.createTokenEntity.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(tokenDto))
        super.setCandidate(super.createTokenEntity)

        return tokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.getToken(wfTokenDto.tokenId)
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        wfTokenManagerService.saveToken(token)
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            wfTokenDto.parentTokenId = token.instance.pTokenId
        }
        token.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        token.assigneeId = wfTokenDto.assigneeId
        wfTokenManagerService.saveToken(token)

        return wfTokenDto
    }
}
