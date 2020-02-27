package co.brainz.itsm.auth.dto

import java.io.Serializable

/**
 * 사용자에서 권한 조회시 사용한다.
 */
data class AuthDetailDto(
    var authId: String,
    var authName: String,
    var checked: Boolean
) : Serializable
