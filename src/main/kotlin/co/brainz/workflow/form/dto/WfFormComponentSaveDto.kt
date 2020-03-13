package co.brainz.workflow.form.dto

import java.io.Serializable

class WfFormComponentSaveDto(
        val form: WfFormSaveDto,
        var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
