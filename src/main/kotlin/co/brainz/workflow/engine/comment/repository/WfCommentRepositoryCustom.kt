package co.brainz.workflow.engine.comment.repository

import co.brainz.workflow.engine.comment.entity.WfCommentEntity

interface WfCommentRepositoryCustom {

    fun findByInstanceId(instanceId: String): MutableList<WfCommentEntity>
}
