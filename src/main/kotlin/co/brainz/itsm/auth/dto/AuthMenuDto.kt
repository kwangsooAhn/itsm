package co.brainz.itsm.auth.dto

import java.io.Serializable

/**
 * 권한별 메뉴 매핑 DTO
 */
data class AuthMenuDto(
        var authId: String? = "",
        var menuId: String? = ""
) : Serializable
