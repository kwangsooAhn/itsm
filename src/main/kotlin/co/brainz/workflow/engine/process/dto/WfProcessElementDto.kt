package co.brainz.workflow.engine.process.dto

import java.io.Serializable

data class WfProcessElementDto(
    var process: WfProcessDto? = null,
    var elements: MutableList<WfElementDto>? = null
) : Serializable
