package co.brainz.framework.certification.dto

import java.io.Serializable

data class OAuthDto(
        var userid: String = "",
        var userName: String = "",
        var email: String = "",
        var platform: String = "",
        var oauthKey: String = ""
) : Serializable
