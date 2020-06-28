package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenViewDto(
    val token: MutableMap<String, Any> = HashMap(),
    val instanceId: String,
    val form: RestTemplateFormComponentListDto,
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf(),
    val assignee: RestTemplateTokenAssigneesViewDto
) : Serializable
