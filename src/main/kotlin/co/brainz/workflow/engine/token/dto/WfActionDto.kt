package co.brainz.workflow.engine.token.dto

import java.io.Serializable

data class WfActionDto(
        val name: String,
        val value : String
) : Serializable
