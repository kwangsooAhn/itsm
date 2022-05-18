/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.itsm.role.dto.RoleListDto

interface AliceRoleAuthMapRepositoryCustom {
    fun findAuthByRoles(roleIds: Set<String>): List<AliceAuthSimpleDto>

    fun findRoleByAuths(auths: String): List<RoleListDto>
}
