package co.brainz.workflow.engine.comment.dto

import java.io.Serializable
import java.time.LocalDateTime

class WfCommentDto(
    var instanceId: String? = null,
    var tokenId: String? = null,
    var commentId: String? = null,
    var content: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null
) : Serializable
