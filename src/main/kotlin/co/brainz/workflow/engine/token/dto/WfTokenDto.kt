package co.brainz.workflow.engine.token.dto

import java.io.Serializable

data class WfTokenDto(
    var tokenId: String = "",
    var documentId: String? = null,
    var documentName: String? = null,
    var isComplete: Boolean = true,
    var elementId: String = "",
    var elementType: String = "",
    var tokenStatus: String? = null,
    var assigneeId: String? = null,
    var data: List<WfTokenDataDto>? = null,
    val action: String = ""
) : Serializable
