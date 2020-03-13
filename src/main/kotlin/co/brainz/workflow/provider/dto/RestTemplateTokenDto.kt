package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenDto(
        var tokenId: String = "",
        var documentId: String? = null,
        var documentName: String? = null,
        var isComplete: Boolean = true,
        var elementId: String = "",
        var tokenStatus: String? = null,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<RestTemplateTokenDataDto>? = null,
        val action: List<RestTemplateActionDto>? = emptyList()
) : Serializable
