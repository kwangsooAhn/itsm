/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIReturnDto(
    val data: List<CIListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
