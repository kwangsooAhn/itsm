/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserListReturnDto(
    var data: List<UserListDataDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
