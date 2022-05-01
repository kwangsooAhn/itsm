package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfTokenEntity

interface WfTokenRepositoryCustom {
    fun findTokenByInstanceIdIn(instanceId: String): List<WfTokenEntity>

    fun findProcessTokenByAssignee(assignee: String): List<WfTokenEntity>

    fun getLastTokenList(instanceIds: Set<String>): List<WfTokenEntity>

    fun getListRunningTokenList(instanceIds: Set<String>): List<WfTokenEntity>
}
