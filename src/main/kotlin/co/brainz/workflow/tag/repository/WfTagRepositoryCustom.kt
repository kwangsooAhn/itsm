package co.brainz.workflow.tag.repository

import co.brainz.workflow.provider.dto.RestTemplateTagViewDto

interface WfTagRepositoryCustom {

    fun findByInstanceId(instanceId: String): List<RestTemplateTagViewDto>
}
