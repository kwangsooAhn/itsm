package co.brainz.workflow.comment.repository

import co.brainz.workflow.comment.entity.WfCommentEntity

interface WfCommentRepositoryCustom {

    fun findByInstanceId(instanceId: String): MutableList<WfCommentEntity>
}
