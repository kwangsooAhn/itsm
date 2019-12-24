package co.brainz.itsm.certification

import java.io.Serializable

data class OAuthDto(
        var email: String = "",
        var name: String? = "",
        var serviceType: String? = ""
) : Serializable