/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.dto

data class AliceUrlDto(
    val url: String = "",
    val method: String = "",
    val urlDesc: String? = null,
    val isRequiredAuth: Boolean? = true
)
