package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateCommentDto(
    var tokenId: String? = null,
    var instanceId: String? = null,
    var commentId: String? = null,
    var content: String? = null,
    var createUserKey: String? = null,
    var createUserName: String? = null,
    var createDt: LocalDateTime? = null,
    var avatarPath: String? = null
) : Serializable
