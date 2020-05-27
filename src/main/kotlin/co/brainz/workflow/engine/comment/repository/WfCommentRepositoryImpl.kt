package co.brainz.workflow.engine.comment.repository

import co.brainz.workflow.engine.comment.entity.QWfCommentEntity
import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfCommentRepositoryImpl : QuerydslRepositorySupport(WfCommentEntity::class.java), WfCommentRepositoryCustom {

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
