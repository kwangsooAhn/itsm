package co.brainz.workflow.engine.form.repository

import co.brainz.workflow.engine.form.entity.WfFormEntity

interface WfFormRepositoryCustom {

    fun findFormEntityList(search: String, status: List<String>): List<WfFormEntity>
}
