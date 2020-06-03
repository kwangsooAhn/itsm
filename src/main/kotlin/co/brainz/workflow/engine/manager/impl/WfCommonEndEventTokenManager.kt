package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEventTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    override fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        wfTokenDto.isAutoComplete = false //반복문을 종료한다.

        if (!wfTokenDto.parentTokenId.isNullOrEmpty()) { // SubProcess, Signal
            val pTokenId = wfTokenDto.parentTokenId!!
            val mainProcessToken = wfTokenManagerService.getToken(pTokenId)
            val elementType = mainProcessToken.element.elementType
            if (elementType == WfElementConstants.ElementType.SUB_PROCESS.value) {
                var token = wfTokenManagerService.getToken(wfTokenDto.tokenId)
                token.tokenData = super.setTokenData(wfTokenDto)
                mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
                mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                wfTokenDto.data = wfTokenManagerService.makeSubProcessTokenDataDto(
                    token,
                    mainProcessToken
                )
                wfTokenDto.tokenId = mainProcessToken.tokenId

                token = wfTokenManagerService.saveToken(mainProcessToken)
                token.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
                wfTokenDto.isAutoComplete = true
            }
        }

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        wfTokenManagerService.completeInstance(wfTokenDto.instanceId)

        return wfTokenDto
    }
}
