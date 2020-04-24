package co.brainz.workflow.engine.comment.dto

import java.io.Serializable

class WfCommentDto(
    val instanceId: String? = null,
    val tokenId: String? = null,
    val comment: String? = null,
    val createUserKey: String
) : Serializable
