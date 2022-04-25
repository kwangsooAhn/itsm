/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class RoleRepositoryImpl : QuerydslRepositorySupport(
    AliceRoleEntity::class.java
), RoleRepositoryCustom {

    override fun findRoleSearch(roleSearchCondition: RoleSearchCondition): List<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val query = from(role)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .where(
                super.likeIgnoreCase(role.roleName, roleSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(role.roleDesc, roleSearchCondition.searchValue))
            )
            .orderBy(role.roleName.asc())
        if (roleSearchCondition.isPaging) {
            query.limit(roleSearchCondition.contentNumPerPage)
            query.offset((roleSearchCondition.pageNum - 1) * roleSearchCondition.contentNumPerPage)
        }
        return query.fetch()
    }

    override fun findByRoleAll(): MutableList<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        return from(role)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .fetch()
    }
}
