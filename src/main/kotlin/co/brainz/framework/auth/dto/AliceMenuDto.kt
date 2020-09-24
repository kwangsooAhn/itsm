package co.brainz.framework.auth.dto

import java.io.Serializable

data class AliceMenuDto(
    val menuId: String = "",
    val pMenuId: String? = "",
    val url: String? = ""
) : Serializable
