package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceRoleEntity

interface AliceUserRoleMapRepositoryCustom {
    fun findUserRoleByUserKey(userKey: String): MutableList<AliceRoleEntity>
}
