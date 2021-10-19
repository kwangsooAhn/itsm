package co.brainz.itsm.comment.repository

import co.brainz.itsm.comment.entity.WfCommentEntity

interface CommentRepositoryCustom {
    fun findByInstanceId(instanceId: String): MutableList<WfCommentEntity>
}
