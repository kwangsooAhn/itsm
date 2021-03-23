/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CITypeReturnDto(
    val data: List<CITypeListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
