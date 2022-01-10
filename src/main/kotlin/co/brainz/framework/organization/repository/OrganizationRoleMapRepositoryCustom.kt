/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.role.dto.RoleListDto

interface OrganizationRoleMapRepositoryCustom : AliceRepositoryCustom {

    fun findRoleListByOrganizationId(organizationId: String): MutableList<RoleListDto>
}
