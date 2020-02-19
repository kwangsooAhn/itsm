package co.brainz.workflow.form.dto

import java.io.Serializable

class FormComponentSaveDto(
        val form: FormSaveDto,
        var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
