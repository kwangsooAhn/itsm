/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.dto

import java.io.Serializable

data class PCodeDto(
    var code: String = "",
    var codeValue: String? = null,
    var codeName: String? = null,
    var pCode: String? = null
) : Serializable
