package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateCommentDto(
    val tokenId: String? = null,
    val instanceId: String? = null,
    val commentId: String? = null,
    val content: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null
) : Serializable
