package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateFormListReturnDto(
    val data: List<RestTemplateFormDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
