package co.brainz.workflow.form.dto

import java.io.Serializable

data class WfFormComponentViewDto(
        val form: WfFormViewDto,
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
): Serializable
