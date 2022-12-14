/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.workflow.service

import co.brainz.api.constants.ApiConstants
import co.brainz.api.dto.RequestDto
import co.brainz.itsm.token.service.TokenService
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.stereotype.Service

@Service
class ApiWorkflowMapper(
    private val userService: UserService,
    private val tokenService: TokenService
) {

    fun callDataMapper(documentId: String, requestDto: RequestDto): RestTemplateTokenDto {
        // 특정 컴포넌트 데이터 변환 (CI)
        requestDto.componentData?.let { tokenService.componentDataConverter(it) }

        val tokenDto = RestTemplateTokenDto(
            instanceId = requestDto.instanceId,
            tokenId = requestDto.tokenId,
            documentId = documentId,
            action = requestDto.action,
            data = requestDto.componentData
        )

        // 담당자가 없을 경우 기본값을 셋팅한다.
        if (!requestDto.assigneeId.isNullOrEmpty()) {
            tokenDto.instanceCreateUser = userService.selectUserKey(requestDto.assigneeId!!)
        } else {
            tokenDto.instanceCreateUser = userService.selectUser(ApiConstants.CREATE_USER)
        }
        tokenDto.assigneeId = tokenDto.instanceCreateUser?.userKey

        return tokenDto
    }
}
