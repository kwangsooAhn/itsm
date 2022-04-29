/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleSearchCondition
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class RoleRepositoryImpl : QuerydslRepositorySupport(
    AliceRoleEntity::class.java
), RoleRepositoryCustom {

    override fun findRoleSearch(roleSearchCondition: RoleSearchCondition): PagingReturnDto {
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
            .where(builder(roleSearchCondition, role))
            .orderBy(role.roleName.asc())
        if (roleSearchCondition.isPaging) {
            query.limit(roleSearchCondition.contentNumPerPage)
            query.offset((roleSearchCondition.pageNum - 1) * roleSearchCondition.contentNumPerPage)
        }

        val countQuery = from(role)
            .select(role.count())
            .where(builder(roleSearchCondition, role))
        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
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

    private fun builder(roleSearchCondition: RoleSearchCondition, role: QAliceRoleEntity):BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(role.roleName, roleSearchCondition.searchValue)
            ?.or(super.likeIgnoreCase(role.roleDesc, roleSearchCondition.searchValue)))
        return builder
    }
}
