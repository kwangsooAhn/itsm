/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.entity.OrganizationRoleMapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRoleMapRepository : JpaRepository<OrganizationRoleMapEntity, String>, OrganizationRoleMapRepositoryCustom {

    fun deleteByOrganization(organization: OrganizationEntity)
}
