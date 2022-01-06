/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.dto

import co.brainz.itsm.role.dto.RoleListDto
import java.io.Serializable

data class OrganizationDetailDto(
    var organizationId: String = "",
    var organizationName: String? = "",
    var pOrganizationId: String? = "",
    var pOrganizationName: String? = "",
    var organizationDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var editable: Boolean? = true,
    var roles: List<RoleListDto> = emptyList()
) : Serializable


