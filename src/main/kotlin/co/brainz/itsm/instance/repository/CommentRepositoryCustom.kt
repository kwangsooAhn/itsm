package co.brainz.itsm.instance.repository

import co.brainz.itsm.instance.entity.WfCommentEntity

interface CommentRepositoryCustom {
    fun findByInstanceId(instanceId: String): MutableList<WfCommentEntity>
}
