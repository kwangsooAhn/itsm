/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.dto

import java.io.Serializable

data class CodeLangDto(
    val codeLangValue: String? = null,
    val lang: String? = null
) : Serializable