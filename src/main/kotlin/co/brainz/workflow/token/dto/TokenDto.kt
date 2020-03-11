package co.brainz.workflow.token.dto

import co.brainz.workflow.token.entity.TokenDataEntity
import java.io.Serializable

data class TokenDto(
        var tokenId: String = "",
        var documentId: String,
        var documentName: String,
        var isComplete: Boolean = true,
        var elementId: String,
        var tokenStatus: String? = null,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        //var data: List<TokenDataEntity>? = null
        var data: List<TokenDataDto>? = null
) : Serializable
