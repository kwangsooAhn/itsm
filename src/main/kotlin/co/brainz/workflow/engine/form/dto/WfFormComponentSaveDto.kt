package co.brainz.workflow.engine.form.dto

import java.io.Serializable

class WfFormComponentSaveDto(
    val form: WfFormDto,
    var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
