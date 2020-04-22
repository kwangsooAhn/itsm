package co.brainz.itsm.auth.dto

import java.io.Serializable

/**
 * 권한별 URL 매핑 DTO
 */
data class AuthUrlDto(
    var url: String? = "",
    var method: String? = "",
    var authId: String? = ""
) : Serializable
