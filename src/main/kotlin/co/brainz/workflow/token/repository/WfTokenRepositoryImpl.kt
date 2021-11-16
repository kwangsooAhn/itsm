package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.QWfTokenEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfTokenRepositoryImpl : QuerydslRepositorySupport(WfTokenEntity::class.java),
    WfTokenRepositoryCustom {
    override fun findTokenByInstanceIdIn(instanceId: String): QueryResults<WfTokenEntity> {
        val token = QWfTokenEntity.wfTokenEntity
        return from(token)
            .select(token)
            .where(token.instance.instanceId.eq(instanceId))
            .fetchResults()
    }
}
