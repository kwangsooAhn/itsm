package co.brainz.itsm.provider.dto

import java.io.Serializable

data class TokenDto(
        var tokenId: String = "",
        var documentId: String = "",
        var documentName: String = "",
        var isComplete: Boolean = true,
        var elementId: String = "",
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<TokenDataDto>? = null
) : Serializable
