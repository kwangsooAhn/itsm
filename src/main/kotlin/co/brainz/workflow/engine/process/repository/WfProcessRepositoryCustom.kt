package co.brainz.workflow.engine.process.repository

import co.brainz.workflow.engine.process.entity.WfProcessEntity

interface WfProcessRepositoryCustom {

    fun findProcessEntityList(search: String, status: List<String>): List<WfProcessEntity>
}
