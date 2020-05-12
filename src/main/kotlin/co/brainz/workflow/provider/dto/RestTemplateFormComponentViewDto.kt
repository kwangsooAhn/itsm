package co.brainz.workflow.provider.dto

import co.brainz.workflow.provider.dto.RestTemplateActionDto
import java.io.Serializable

data class RestTemplateFormComponentViewDto(
    val form: RestTemplateFormDto,
    val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf()
) : Serializable
