package co.brainz.workflow.engine.token.dto

import java.io.Serializable

data class WfTokenDataDto(
    val componentId: String,
    var value: String
) : Serializable
