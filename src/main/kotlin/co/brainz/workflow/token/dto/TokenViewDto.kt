package co.brainz.workflow.token.dto

import java.io.Serializable

data class TokenViewDto(
        val tokenId: String,
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
        val action: MutableList<ActionDto> = mutableListOf()
) : Serializable
