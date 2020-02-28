package co.brainz.workflow.process.dto

import java.io.Serializable

data class WfJsonMainDto(
    var process: WfJsonProcessDto? = null,
    var elements: MutableList<WfJsonElementDto>? = null
) : Serializable
