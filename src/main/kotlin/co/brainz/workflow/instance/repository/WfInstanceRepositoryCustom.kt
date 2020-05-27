package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.WfInstanceListViewDto

interface WfInstanceRepositoryCustom {

    fun findInstances(status: String): List<WfInstanceListViewDto>
}
