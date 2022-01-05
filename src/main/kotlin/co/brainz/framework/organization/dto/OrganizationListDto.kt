/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.dto

import java.io.Serializable
import java.time.LocalDateTime

data class OrganizationListDto(
    var organizationId: String? = null,
    var pOrganizationId: String? = null,
    var organizationName: String? = null,
    var organizationDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var editable: Boolean? = true,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
