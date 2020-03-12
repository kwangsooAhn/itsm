package co.brainz.itsm.provider.dto

import java.io.Serializable

data class ActionDto(
        val name: String,
        val value : String
) : Serializable
