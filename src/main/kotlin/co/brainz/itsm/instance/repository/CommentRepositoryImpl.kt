package co.brainz.itsm.instance.repository

import co.brainz.itsm.instance.entity.QWfCommentEntity
import co.brainz.itsm.instance.entity.WfCommentEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl : QuerydslRepositorySupport(WfCommentEntity::class.java),
    CommentRepositoryCustom {

    override fun findByInstanceId(instanceId: String): MutableList<WfCommentEntity> {
        val comment = QWfCommentEntity.wfCommentEntity

        return from(comment)
            .innerJoin(comment.instance)
            .fetchJoin()
            .innerJoin(comment.aliceUserEntity)
            .fetchJoin()
            .where(comment.instance.instanceId.eq(instanceId))
            .fetch()
    }
}
