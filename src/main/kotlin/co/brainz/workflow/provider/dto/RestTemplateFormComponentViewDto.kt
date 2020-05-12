package co.brainz.workflow.provider.dto

import co.brainz.workflow.engine.token.dto.WfActionDto
import java.io.Serializable

data class RestTemplateFormComponentViewDto(
    val form: RestTemplateFormDto,
    val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
    val actions: MutableList<WfActionDto>? = mutableListOf()
) : Serializable
