package co.brainz.workflow.token.dto

import java.io.Serializable

data class WfActionDto(
        val name: String,
        val value : String
) : Serializable
