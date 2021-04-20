/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CodeDto(
    var code: String = "",
    var pCode: String? = null,
    var codeValue: String? = null,
    var codeName: String? = null,
    var codeDesc: String? = null,
    var editable: Boolean? = true,
    var createDt: LocalDateTime? = null,
    var level: Int? = null,
    var seqNum: Int? = null,
    var codeLangValue: String? = null,
    var lang: String? = null
) : Serializable
