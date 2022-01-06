/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.organization.entity.OrganizationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<OrganizationEntity, String>, OrganizationRepositoryCustom {
    fun findByOrganizationId(organizationId: String): OrganizationEntity
    fun deleteByOrganizationId(organizationId: String)
}
