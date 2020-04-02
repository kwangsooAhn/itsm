package co.brainz.workflow.engine.form.dto

import co.brainz.workflow.engine.token.dto.WfActionDto
import java.io.Serializable

data class WfFormComponentViewDto(
        val form: WfFormViewDto,
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf(),
        val action: MutableList<WfActionDto>? = mutableListOf()
): Serializable
