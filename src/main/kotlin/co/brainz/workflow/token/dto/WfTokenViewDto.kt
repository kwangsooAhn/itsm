package co.brainz.workflow.token.dto

import java.io.Serializable

data class WfTokenViewDto(
        val tokenId: String,
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
        val action: MutableList<WfActionDto> = mutableListOf()
) : Serializable
