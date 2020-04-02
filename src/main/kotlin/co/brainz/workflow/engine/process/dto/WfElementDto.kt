package co.brainz.workflow.engine.process.dto

import java.io.Serializable

data class WfElementDto(
    var id: String = "",
    var type: String = "",
    var display: MutableMap<String, Any>? = null,
    var data: MutableMap<String, Any>? = null
) : Serializable
