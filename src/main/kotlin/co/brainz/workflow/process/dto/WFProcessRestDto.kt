package co.brainz.workflow.process.dto

import java.io.Serializable

data class WFProcessRestDto(
    var process: WFProcessDto? = null,
    var elements: MutableList<WFElementDto>? = null
) : Serializable
