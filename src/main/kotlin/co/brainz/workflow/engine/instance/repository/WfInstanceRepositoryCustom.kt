package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto

interface WfInstanceRepositoryCustom {

    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>
}
