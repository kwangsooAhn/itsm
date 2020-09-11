package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface RoleRepositoryCustom : AliceRepositoryCustom {

    fun findRoleSearch(search: String): QueryResults<AliceRoleEntity>
}
