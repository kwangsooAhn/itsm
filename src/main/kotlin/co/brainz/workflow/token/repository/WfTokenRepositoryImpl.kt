package co.brainz.workflow.token.repository

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfTokenRepositoryImpl : QuerydslRepositorySupport(WfTokenEntity::class.java),
    WfTokenRepositoryCustom {
    override fun findTokenByInstanceIdIn(instanceId: String): List<WfTokenEntity> {
        val token = QWfTokenEntity.wfTokenEntity
        return from(token)
            .select(token)
            .where(token.instance.instanceId.eq(instanceId))
            .fetch()
    }

    override fun findProcessTokenByAssignee(assignee: String): List<WfTokenEntity> {
        val token = QWfTokenEntity.wfTokenEntity
        return from(token)
            .where(token.tokenStatus.eq(WfTokenConstants.Status.RUNNING.code))
            .where(token.assigneeId.eq(assignee))
            .fetch()
    }

    override fun getEndTokenList(instanceIds: Set<String>): List<WfTokenEntity> {
        val token = QWfTokenEntity.wfTokenEntity
        return from(token)
            .innerJoin(token.element).fetchJoin()
            .where(token.instance.instanceId.`in`(instanceIds))
            .where(token.element.elementType.eq(WfElementConstants.ElementType.COMMON_END_EVENT.value))
            .fetch()
    }

    override fun getListRunningTokenList(instanceIds: Set<String>): List<WfTokenEntity> {
        val token = QWfTokenEntity.wfTokenEntity
        return from(token)
            .innerJoin(token.element).fetchJoin()
            .where(token.tokenStatus.`in`(WfTokenConstants.Status.RUNNING.code, WfTokenConstants.Status.WAITING.code))
            .where(token.instance.instanceId.`in`(instanceIds))
            .fetch()
    }

    override fun getLastToken(instanceId: String): WfTokenEntity {
        val token = QWfTokenEntity.wfTokenEntity
        val tokenMaxStartDt = QWfTokenEntity.wfTokenEntity

        return from(token)
            .where(token.tokenStartDt.eq(
                from(tokenMaxStartDt)
                    .select(tokenMaxStartDt.tokenStartDt.max())
                    .where(tokenMaxStartDt.instance.instanceId.eq(instanceId))
            ))
            .where(token.instance.instanceId.eq(instanceId))
            .fetchOne()
    }
}
