/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.dto

import java.io.Serializable

data class RoleListReturnDto(
    val data: List<RoleListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
