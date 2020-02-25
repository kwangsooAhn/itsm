package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenDataDto(
        val componentId: String,
        var value: String
) : Serializable
