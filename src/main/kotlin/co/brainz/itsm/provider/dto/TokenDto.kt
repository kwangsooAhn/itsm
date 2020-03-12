package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenDto(
        var tokenId: String = "",
        var documentId: String? = null,
        var documentName: String? = null,
        var isComplete: Boolean = true,
        var elementId: String = "",
        var tokenStatus: String? = null,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<TokenDataDto>? = null,
        val action: List<ActionDto>? = emptyList()
) : Serializable
