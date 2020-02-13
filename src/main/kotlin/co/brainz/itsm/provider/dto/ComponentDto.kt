package co.brainz.itsm.provider.dto

import java.io.Serializable

data class ComponentDto(
        val id: String = "",
        val type: String = "",
        val label: String = "",
        val display: String = "",
        val validate: String = ""
) : Serializable
