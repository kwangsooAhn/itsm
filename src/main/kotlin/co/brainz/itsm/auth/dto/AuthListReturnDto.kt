/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class AuthListReturnDto(
    var data: List<AuthListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
