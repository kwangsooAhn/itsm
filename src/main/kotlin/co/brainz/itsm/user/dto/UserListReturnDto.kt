/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class UserListReturnDto(
    val data: List<UserListDataDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
