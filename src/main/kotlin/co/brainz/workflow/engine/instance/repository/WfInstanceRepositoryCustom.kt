package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto

interface WfInstanceRepositoryCustom {

    fun findInstances(status: String): List<WfInstanceListViewDto>
}
