/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.dto

import java.io.Serializable

data class AliceMenuDto(
    val menuId: String = "",
    val pMenuId: String = "",
    val url: String = "",
    val sort: Int = 0,
    val useYn: Boolean = true
) : Serializable
