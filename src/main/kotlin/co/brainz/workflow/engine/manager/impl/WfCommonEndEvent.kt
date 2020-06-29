/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEvent(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = true
) : WfTokenManager(wfTokenManagerService) {
    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        return createNextTokenDto
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.tokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(super.setTokenData(completedToken))
        wfTokenManagerService.completeInstance(completedToken.instanceId)

        if (!completedToken.parentTokenId.isNullOrEmpty()) { // SubProcess, Signal
            val pTokenId = completedToken.parentTokenId!!
            val mainProcessToken = wfTokenManagerService.getToken(pTokenId)
            when (mainProcessToken.element.elementType) {
                WfElementConstants.ElementType.SUB_PROCESS.value -> {
                    var token = wfTokenManagerService.getToken(completedToken.tokenId)
                    token.tokenDataEntities = super.setTokenData(completedToken)
                    mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
                    mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                    completedToken.data = wfTokenManagerService.makeSubProcessTokenDataDto(
                        token,
                        mainProcessToken
                    )
                    completedToken.tokenId = mainProcessToken.tokenId

                    token = wfTokenManagerService.saveToken(mainProcessToken)
                    token.tokenDataEntities =
                        wfTokenManagerService.saveAllTokenData(super.setTokenData(completedToken))
                }
            }
        }

        return completedToken
    }
}
