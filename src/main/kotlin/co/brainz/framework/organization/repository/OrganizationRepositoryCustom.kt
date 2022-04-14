/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface OrganizationRepositoryCustom : AliceRepositoryCustom {
    fun findByOrganizationSearchList(organizationSearchCondition: OrganizationSearchCondition): QueryResults<OrganizationEntity>
    fun existsByOrganizationName(organizationName: String, organizationId: String?): Long
    fun getOrganizationListByIds(organizationIds: Set<String>): List<OrganizationEntity>
    fun existsByPOrganizationId(organizationId: String): Boolean
    fun findOrganizationsByUseYn(): QueryResults<OrganizationEntity>
}
