/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

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
                super.like(role.roleName, search)
                    ?.or(super.like(role.roleDesc, search))
            ).orderBy(role.roleName.asc())
            .fetchResults()
    }
}
