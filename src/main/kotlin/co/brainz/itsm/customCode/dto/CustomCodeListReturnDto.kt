/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CustomCodeListReturnDto(
    val data: MutableList<CustomCodeListDto>,
    val paging: AlicePagingData
) : Serializable
