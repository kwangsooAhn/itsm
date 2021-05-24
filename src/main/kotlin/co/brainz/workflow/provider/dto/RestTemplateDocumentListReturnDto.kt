package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentListReturnDto(
    val data: List<RestTemplateDocumentListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
