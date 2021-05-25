package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateInstanceListReturnDto(
    val data: List<RestTemplateInstanceViewDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
