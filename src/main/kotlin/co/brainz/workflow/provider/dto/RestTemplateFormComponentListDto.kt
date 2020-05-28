package co.brainz.workflow.provider.dto

import java.io.Serializable

class RestTemplateFormComponentListDto(
    var id: String = "",
    val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
