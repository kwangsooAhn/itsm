/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.api.constants.ApiConstants
import co.brainz.api.dto.RequestDto
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.stereotype.Service

@Service
class ApiWorkflowMapper(
    private val userService: UserService
) {

    fun callDataMapper(documentId: String, requestDto: RequestDto): RestTemplateTokenDto {
        val tokenDto = RestTemplateTokenDto(
            instanceId = AliceUtil().getUUID(),
            tokenId = requestDto.tokenId,
            documentId = documentId,
            action = requestDto.action,
            data = requestDto.componentData
        )

        // 담당자가 없을 경우 기본값을 셋팅한다.
        if (!requestDto.assigneeId.isNullOrEmpty()) {
            tokenDto.instanceCreateUser = userService.selectUser(requestDto.assigneeId!!)
        } else {
            tokenDto.instanceCreateUser = userService.selectUser(ApiConstants.CREATE_USER)
        }
        tokenDto.assigneeId = tokenDto.instanceCreateUser?.userKey

        return tokenDto
    }
}
