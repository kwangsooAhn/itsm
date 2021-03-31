/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.dto

import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.time.LocalDateTime

/**
 * 역할 조회시 사용한다.
 */
data class RoleDto(
    @CheckUnacceptableCharInUrl
    var roleId: String?,
    var roleName: String?,
    var roleDesc: String?,
    var createUserName: String?,
    var createDt: LocalDateTime?,
    var updateUserName: String?,
    var updateDt: LocalDateTime?,
    var arrAuthId: MutableSet<String>?,
    var arrAuthList: List<AliceAuthSimpleDto>?,
    var userRoleMapCount: Int
)
