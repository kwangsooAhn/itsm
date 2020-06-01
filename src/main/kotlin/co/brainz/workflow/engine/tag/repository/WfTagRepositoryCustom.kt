package co.brainz.workflow.engine.tag.repository

import co.brainz.workflow.engine.tag.entity.WfTagEntity

interface WfTagRepositoryCustom {

    fun findByInstanceId(instanceId: String): MutableList<WfTagEntity>
}
