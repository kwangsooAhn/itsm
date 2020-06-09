package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.WfFormEntity

interface WfFormRepositoryCustom {

    fun findFormEntityList(search: String, status: List<String>): List<WfFormEntity>
}
