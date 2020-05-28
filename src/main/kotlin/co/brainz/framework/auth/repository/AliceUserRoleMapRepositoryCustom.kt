package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity

interface AliceUserRoleMapRepositoryCustom {
    fun findUserRoleByUserKey(userKey: String): MutableList<AliceRoleEntity>
    fun findUserRoleMapByRoleId(roleId: String): MutableList<AliceUserRoleMapEntity>?
}
