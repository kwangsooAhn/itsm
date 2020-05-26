package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenDto(
    var tokenId: String = "",
    var documentId: String? = null,
    var documentName: String? = null,
    var instanceId: String? = null,
    var isComplete: Boolean = true,
    var elementId: String = "",
    var elementType: String = "",
    var tokenStatus: String? = null,
    var assigneeId: String? = null,
    var data: List<RestTemplateTokenDataDto>? = null,
    var fileDataIds: String? = null,
    val actions: List<RestTemplateActionDto>? = emptyList(),
    val action: String? = null,
    val numberingId: String? = null,
    var parentTokenId: String? = null
) : Serializable
