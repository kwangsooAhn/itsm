package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateProcessListReturnDto(
    val data: List<RestTemplateProcessViewDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
