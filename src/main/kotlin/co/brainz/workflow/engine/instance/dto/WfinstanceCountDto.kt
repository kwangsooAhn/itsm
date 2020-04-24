package co.brainz.workflow.engine.instance.dto

import java.io.Serializable

data class WfInstanceCountDto(
    val instanceStatus: String,
    val instanceCount: Int? = 0
) : Serializable