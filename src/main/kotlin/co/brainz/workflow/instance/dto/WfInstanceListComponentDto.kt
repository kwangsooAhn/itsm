package co.brainz.workflow.instance.dto

import java.io.Serializable

data class WfInstanceListComponentDto(
    val componentId: String,
    val componentType: String,
    val mappingId: String? = "",
    val isTopic: Boolean = false,
    val tokenId: String
) : Serializable
