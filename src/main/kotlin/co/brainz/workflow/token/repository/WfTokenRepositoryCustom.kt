package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfTokenEntity
import com.querydsl.core.QueryResults

interface WfTokenRepositoryCustom {
    fun findTokenByInstanceIdIn(instanceId: String): QueryResults<WfTokenEntity>

    fun findProcessTokenByAssignee(assignee: String): List<WfTokenEntity>
}
