/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.document.constants.WfDocumentConstants
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

        // CMDB 임시테이블에 등록된 자산 정보가 있을 경우 삭제한다.
        // CI 컴포넌트와 관련된 신청서의 상태가 '임시'인 경우, CMDB 임시테이블에 등록된 자선 정보를 삭제하지 않는다.
        val documentStatus = createTokenDto.instanceId?.let { instanceId ->
            wfTokenManagerService.getInstance(instanceId)?.let { WfInstanceEntity ->
                wfTokenManagerService.getDocument(WfInstanceEntity.document.documentId).documentStatus
            }
        }

        if (documentStatus != WfDocumentConstants.Status.TEMPORARY.code) {
            val ciComponentDataList = wfTokenManagerService.getComponentCiDataList(createTokenDto.instanceId)
            if (!ciComponentDataList.isNullOrEmpty()) {
                wfTokenManagerService.deleteCiComponentData(ciComponentDataList)
            }
        }
        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto? {
        var nextTokenDto: WfTokenDto? = null

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
                    nextTokenDto = createNextTokenDto
                }
            }
        }
        return nextTokenDto
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.tokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(super.setTokenData(completedToken))
        wfTokenManagerService.completeInstance(completedToken.instanceId)

        return completedToken
    }
}
