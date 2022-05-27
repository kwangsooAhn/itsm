package co.brainz.workflow.token.repository

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.jpa.JPAExpressions
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

    override fun getLastTokenIdList(instanceIds: Set<String>): List<Map<String,String>> {
        val token = QWfTokenEntity.wfTokenEntity
        val token2 = QWfTokenEntity.wfTokenEntity
        val token3 = QWfTokenEntity.wfTokenEntity
        val instance = QWfInstanceEntity.wfInstanceEntity

        // select wi.instance_id, max(wt.token_start_dt) from wf_instance wi, wf_token wt where wi.instance_id in ('492415df395d48d68d5ddbb7ccc33e57','3b49b6efdf994c5f8a37e376a383477a')
        // and wi.instance_id = wt.instance_id
        //     group by wi.instance_id

        val temp =


        from(token2)
            .where(token2.tokenStartDt.eq(
                from(token)
                    .select(token.tokenStartDt.max())
                    .join(instance).on(token.instance.eq(instance))
                    .where(instance.instanceId.`in`(instanceIds))
                    .groupBy(instance.instanceId)
            ))
            .where(instance.instanceId.`in`(instanceIds))
    }
}
