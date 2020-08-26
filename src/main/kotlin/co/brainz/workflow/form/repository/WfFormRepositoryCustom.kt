package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.WfFormEntity
import com.querydsl.core.QueryResults

interface WfFormRepositoryCustom {

    fun findFormEntityList(search: String, status: List<String>, offset: Long): QueryResults<WfFormEntity>
}
