package co.brainz.itsm.comment.dto

import java.io.Serializable
import java.time.LocalDateTime

data class InstanceCommentDto(
    var tokenId: String? = null,
    var instanceId: String? = null,
    var commentId: String? = null,
    var content: String? = null,
    var createUserKey: String? = null,
    var createUserName: String? = null,
    var createDt: LocalDateTime? = null,
    var avatarPath: String? = null
) : Serializable
