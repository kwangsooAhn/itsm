package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto

interface WfInstanceRepositoryCustom {

    fun findInstances(status: String): List<WfInstanceListViewDto>
    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>
}
