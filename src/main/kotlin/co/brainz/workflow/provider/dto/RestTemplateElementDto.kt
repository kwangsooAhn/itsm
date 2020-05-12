package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateElementDto(
    var id: String = "",
    var type: String = "",
    var display: MutableMap<String, Any>? = null,
    var data: MutableMap<String, Any>? = null
) : Serializable
