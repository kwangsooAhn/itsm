package co.brainz.framework.certification.dto

import java.io.Serializable

data class OAuthDto(
        var userid: String = "",
        var email: String = "",
        var platform: String = "",
        var userName: String = ""
) : Serializable
