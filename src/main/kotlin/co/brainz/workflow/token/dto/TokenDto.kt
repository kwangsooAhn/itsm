package co.brainz.workflow.token.dto

import java.io.Serializable

data class TokenDto(
        var id: String = "",
        val isComplete: Boolean = true,
        val elemId: String,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<TokenDataDto>? = null
) : Serializable
