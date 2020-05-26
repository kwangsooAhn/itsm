package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateElementDto(
    var id: String = "",
    var name: String = "",
    var type: String = "",
    var notification: String = "N",
    var description: String = "",
    var display: MutableMap<String, Any>? = null,
    var data: MutableMap<String, Any>? = null,
    var required: MutableList<String>? = null
) : Serializable
