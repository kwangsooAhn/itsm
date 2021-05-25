/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.dto

import java.io.Serializable

data class PortalListReturnDto(
    val data: List<PortalDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
