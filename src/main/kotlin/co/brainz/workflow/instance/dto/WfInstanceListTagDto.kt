package co.brainz.workflow.instance.dto

import java.io.Serializable

data class WfInstanceListTagDto(
    val tagId: String,
    val tagContent: String,
    val instanceId: String
) : Serializable
