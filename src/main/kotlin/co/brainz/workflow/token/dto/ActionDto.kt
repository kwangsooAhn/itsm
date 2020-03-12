package co.brainz.workflow.token.dto

import java.io.Serializable

data class ActionDto(
        val name: String,
        val value : String
) : Serializable
