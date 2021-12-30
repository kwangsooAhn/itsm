package co.brainz.itsm.group.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface GroupRoleMapRepositoryCustom : AliceRepositoryCustom {

    fun findGroupRoleMapByGroupId(groupId: String): MutableList<AliceRoleEntity>

}
