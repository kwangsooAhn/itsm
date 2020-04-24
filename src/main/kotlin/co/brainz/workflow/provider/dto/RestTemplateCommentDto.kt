package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateCommentDto(
    val tokenId: String? = null,
    val instanceId: String? = null,
    val comment: String? = null
) : Serializable
