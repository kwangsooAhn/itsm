/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.dto

import java.io.Serializable

data class CodeReturnDto(
    val data: List<CodeDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
