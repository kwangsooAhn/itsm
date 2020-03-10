package co.brainz.workflow.token.dto

import java.io.Serializable

data class TokenDto(
        var tokenId: String = "",
        var isComplete: Boolean = true,
        var elementId: String,
        var tokenStatus: String? = null,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<TokenDataDto>? = null
) : Serializable
