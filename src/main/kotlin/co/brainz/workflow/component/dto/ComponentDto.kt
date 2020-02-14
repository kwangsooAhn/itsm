package co.brainz.workflow.component.dto

import java.io.Serializable

data class ComponentDto(
        val id: String = "",
        val type: String = ""
) : Serializable
