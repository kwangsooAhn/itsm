package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class RoleRepositoryImpl : QuerydslRepositorySupport(
    AliceRoleEntity::class.java
), RoleRepositoryCustom {

    override fun findRoleSearch(search: String): QueryResults<AliceRoleEntity> {
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(role)
            .where(
                super.likeIgnoreCase(role.roleId, search)
                    ?.or(super.likeIgnoreCase(role.roleDesc, search))
            ).orderBy(role.roleId.asc())
            .fetchResults()
    }
}
