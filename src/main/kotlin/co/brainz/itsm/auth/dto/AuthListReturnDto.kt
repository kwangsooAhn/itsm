/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.dto

import java.io.Serializable

data class AuthListReturnDto(
    var data: List<AuthListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
