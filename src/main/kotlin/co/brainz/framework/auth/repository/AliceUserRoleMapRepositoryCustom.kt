/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.itsm.role.dto.RoleListDto

interface AliceUserRoleMapRepositoryCustom {
    fun findUserRoleByUserKey(userKey: String): MutableList<RoleListDto>
    fun findUserRoleMapByRoleId(roleId: String): MutableList<AliceUserRoleMapEntity>?
    fun findUserRoleMapByRoleIds(roleIds: MutableList<String>): MutableList<AliceUserRoleMapEntity>
}
