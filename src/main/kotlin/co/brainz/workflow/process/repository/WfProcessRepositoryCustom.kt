package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.WfProcessEntity
import com.querydsl.core.QueryResults

interface WfProcessRepositoryCustom {

    fun findProcessEntityList(search: String, status: List<String>, offset: Long?): QueryResults<WfProcessEntity>
}
