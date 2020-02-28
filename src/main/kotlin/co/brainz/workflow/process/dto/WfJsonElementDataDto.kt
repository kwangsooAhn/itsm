package co.brainz.workflow.process.dto

import java.io.Serializable

data class WfJsonElementDataDto(
    var elementId: String = "",
    var attributeId: String = "",
    var attributeValue: String = "",
    var attr_order: Int = 0
) : Serializable
