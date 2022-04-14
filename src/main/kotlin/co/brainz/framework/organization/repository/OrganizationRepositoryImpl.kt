/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.entity.QOrganizationEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class OrganizationRepositoryImpl : QuerydslRepositorySupport(OrganizationEntity::class.java),
    OrganizationRepositoryCustom {

    override fun findByOrganizationSearchList(
        organizationSearchCondition: OrganizationSearchCondition
    ): QueryResults<OrganizationEntity> {
        val organization = QOrganizationEntity.organizationEntity
        val query = from(organization)
            .where(
                super.likeIgnoreCase(organization.organizationName, organizationSearchCondition.searchValue)
            )
        if (organizationSearchCondition.isPaging) {
            query.limit(organizationSearchCondition.contentNumPerPage)
            query.offset((organizationSearchCondition.pageNum - 1) * organizationSearchCondition.contentNumPerPage)
        }
        query.orderBy(organization.level.asc(), organization.seqNum.asc())
        return query.fetchResults()
    }

    override fun existsByOrganizationName(organizationName: String, organizationId: String?): Long {
        val organization = QOrganizationEntity.organizationEntity
        val query = from(organization)
            .where(organization.organizationName.eq(organizationName))
        if (!organizationId.isNullOrEmpty()) {
            query.where(organization.organizationId.ne(organizationId))
        }
        return query.fetchCount()
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
            .fetchCount() > 0
    }
}
