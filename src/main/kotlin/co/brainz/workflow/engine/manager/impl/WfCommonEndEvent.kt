package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEvent(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {
    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        createNextTokenDto.isAutoComplete = super.setAutoComplete(createNextTokenDto.elementType)
        if (!createNextTokenDto.parentTokenId.isNullOrEmpty()) { // SubProcess, Signal
            val pTokenId = createNextTokenDto.parentTokenId!!
            val mainProcessToken = wfTokenManagerService.getToken(pTokenId)
            when (mainProcessToken.element.elementType) {
                WfElementConstants.ElementType.SUB_PROCESS.value -> {
                    var token = wfTokenManagerService.getToken(createNextTokenDto.tokenId)
                    token.tokenDataEntities = super.setTokenData(createNextTokenDto)
                    mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
                    mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                    createNextTokenDto.data = wfTokenManagerService.makeSubProcessTokenDataDto(
                        token,
                        mainProcessToken
                    )
                    createNextTokenDto.tokenId = mainProcessToken.tokenId

                    token = wfTokenManagerService.saveToken(mainProcessToken)
                    token.tokenDataEntities =
                        wfTokenManagerService.saveAllTokenData(super.setTokenData(createNextTokenDto))
                    createNextTokenDto.isAutoComplete = true
                }
                else -> createNextTokenDto.isAutoComplete = false
            }
        } else {
            createNextTokenDto.isAutoComplete = false
        }

        return createNextTokenDto
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        wfTokenManagerService.completeInstance(completedToken.instanceId)

        return completedToken
    }
}
