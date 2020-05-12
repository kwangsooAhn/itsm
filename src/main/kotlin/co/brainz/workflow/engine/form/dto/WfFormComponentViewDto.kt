package co.brainz.workflow.engine.form.dto

import co.brainz.workflow.provider.dto.RestTemplateActionDto
import java.io.Serializable

data class WfFormComponentViewDto(
    val form: WfFormDto,
    val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf()
) : Serializable
