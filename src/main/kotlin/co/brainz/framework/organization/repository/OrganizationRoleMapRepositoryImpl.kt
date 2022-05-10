/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.entity.OrganizationRoleMapEntity
import co.brainz.framework.organization.entity.QOrganizationRoleMapEntity
import co.brainz.itsm.role.dto.RoleListDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class OrganizationRoleMapRepositoryImpl : QuerydslRepositorySupport(OrganizationRoleMapEntity::class.java),
    OrganizationRoleMapRepositoryCustom {

    override fun findRoleListByOrganizationId(organizationId: String): MutableList<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val organizationRoleMap = QOrganizationRoleMapEntity.organizationRoleMapEntity

        return from(organizationRoleMap)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
            .innerJoin(role).on(organizationRoleMap.role.eq(role))
            .where(organizationRoleMap.organization.eq(OrganizationEntity(organizationId)))
            .fetch()
    }
}
