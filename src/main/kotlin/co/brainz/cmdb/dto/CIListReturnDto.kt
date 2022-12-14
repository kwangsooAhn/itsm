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

data class CIDynamicReturnDto(
    val data: CIDynamicListDto? = null,
    val paging: AlicePagingData
) : Serializable

data class CIModalReturnDto(
    val data: List<CIListDto> = emptyList(),
    val totalCount: Long = 0L
)
