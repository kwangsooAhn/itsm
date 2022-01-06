/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.dto

import java.io.Serializable

data class OrganizationListReturnDto(
    var data: List<OrganizationListDto> = emptyList(),
    var totalCount: Long = 0L
) : Serializable
