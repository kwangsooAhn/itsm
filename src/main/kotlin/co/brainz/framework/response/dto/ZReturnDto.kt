/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.response.dto

import java.io.Serializable

data class ZReturnDto(
    val result: Boolean? = true,
    val message: String? = "",
    val data: Any? = null
) : Serializable
