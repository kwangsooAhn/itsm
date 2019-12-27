package co.brainz.itsm.certification

import java.io.Serializable

data class OAuthDto(
        var userid: String = "",
        var email: String = "",
        var platform: String = ""
) : Serializable