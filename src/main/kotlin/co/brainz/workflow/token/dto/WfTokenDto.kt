package co.brainz.workflow.token.dto

import java.io.Serializable

data class WfTokenDto(
        var tokenId: String = "",
        var documentId: String? = null,
        var documentName: String? = null,
        var isComplete: Boolean = true,
        var elementId: String = "",
        var tokenStatus: String? = null,
        var assigneeId: String? = null,
        var assigneeType: String? = null,
        var data: List<WfTokenDataDto>? = null,
        val action: List<WfActionDto>? = emptyList()
) : Serializable
