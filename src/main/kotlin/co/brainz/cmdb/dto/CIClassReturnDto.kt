/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIClassReturnDto(
    val data: List<CIClassListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
