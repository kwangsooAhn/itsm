/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CustomCodeTreeDto(
    var code: String = "",
    var pCode: String? = null,
    var codeValue: String? = null,
    var codeName: String? = null,
    var codeDesc: String? = null,
    var editable: Boolean? = true,
    var createDt: LocalDateTime? = null,
    var level: Int? = null,
    var seqNum: Int? = null
) : Serializable

data class CustomCodeTreeReturnDto(
    val data: List<CustomCodeTreeDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable

