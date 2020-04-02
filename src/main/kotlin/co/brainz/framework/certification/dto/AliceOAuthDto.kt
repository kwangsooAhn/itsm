package co.brainz.framework.certification.dto

import java.io.Serializable

data class AliceOAuthDto(
        var userId: String = "",
        var userName: String = "",
        var email: String = "",
        var platform: String = "",
        var oauthKey: String = ""
) : Serializable
