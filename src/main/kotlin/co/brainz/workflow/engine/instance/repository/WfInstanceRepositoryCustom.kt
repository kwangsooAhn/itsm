package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto

interface WfInstanceRepositoryCustom {

    fun findInstances(status: String): List<WfInstanceListViewDto>
    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>
}
