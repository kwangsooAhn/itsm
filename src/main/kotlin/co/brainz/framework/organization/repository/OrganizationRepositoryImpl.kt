/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.entity.QOrganizationEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class OrganizationRepositoryImpl : QuerydslRepositorySupport(OrganizationEntity::class.java),
    OrganizationRepositoryCustom {

    override fun findByOrganizationSearchList(
        organizationSearchCondition: OrganizationSearchCondition
    ): List<OrganizationEntity> {
        val organization = QOrganizationEntity.organizationEntity
        val query = from(organization)
            .where(
                super.likeIgnoreCase(organization.organizationName, organizationSearchCondition.searchValue)
            )
        query.orderBy(organization.level.asc(), organization.seqNum.asc())
        return query.fetch()
    }

    override fun getOrganizationListByIds(organizationIds: Set<String>): List<OrganizationEntity> {
        val organization = QOrganizationEntity.organizationEntity
        return from(organization)
            .where(organization.organizationId.`in`(organizationIds))
            .fetch()
    }

    override fun existsByPOrganizationId(organizationId: String): Boolean {
        val organization = QOrganizationEntity.organizationEntity
        return from(organization)
            .where(organization.pOrganization.organizationId.eq(organizationId))
            .fetch().size > 0
    }

    override fun findOrganizationsByUseYn(): List<OrganizationEntity> {
        val organization = QOrganizationEntity.organizationEntity
        return from(organization)
            .where(organization.useYn.eq(true))
            .fetch()
    }
}
