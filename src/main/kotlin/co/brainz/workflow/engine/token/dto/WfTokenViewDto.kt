package co.brainz.workflow.engine.token.dto

import java.io.Serializable

data class WfTokenViewDto(
    val tokenId: String,
    val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
    val actions: MutableList<WfActionDto> = mutableListOf()
) : Serializable
