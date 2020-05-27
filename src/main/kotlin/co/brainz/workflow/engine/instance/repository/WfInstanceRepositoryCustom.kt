package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(status: String): List<WfInstanceListViewDto>
}
