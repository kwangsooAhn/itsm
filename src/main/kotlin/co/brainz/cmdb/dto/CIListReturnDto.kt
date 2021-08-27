/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CIListReturnDto(
    val data: List<CIListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
