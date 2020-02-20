package co.brainz.workflow.form.dto

import java.io.Serializable

data class FormComponentViewDto(
        val form: FormViewDto,
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
): Serializable
