package co.brainz.itsm.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class InstanceCommentDto(
    var instanceId: String? = null,
    var commentId: String? = null,
    var content: String? = null,
    var createUserKey: String? = null,
    var createUserName: String? = null,
    var createDt: LocalDateTime? = null,
    var avatarPath: String? = null
) : Serializable
