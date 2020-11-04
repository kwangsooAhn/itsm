/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceAuthSimpleDto

interface AliceRoleAuthMapRepositoryCustom {
    fun findAuthByRoles(roleIds: Set<String>): List<AliceAuthSimpleDto>
}
