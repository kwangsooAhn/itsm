package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.WfProcessEntity

interface WfProcessRepositoryCustom {

    fun findProcessEntityList(search: String, status: List<String>): List<WfProcessEntity>
}
