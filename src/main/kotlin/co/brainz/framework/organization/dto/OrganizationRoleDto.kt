/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.dto

import java.io.Serializable

data class OrganizationRoleDto(
    var organizationId: String = "",
    var pOrganizationId: String? = null,
    var organizationName: String? = null,
    var organizationDesc: String? = null,
    var useYn: Boolean? = true,
    var seqNum: Int? = null,
    var roleIds: List<String> = emptyList()
) : Serializable
