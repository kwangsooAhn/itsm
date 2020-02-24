package co.brainz.workflow.instance.dto

import java.io.Serializable

data class InstanceDto(
        val instId: String,
        val procId: String,
        val instStatus: String? = null
) : Serializable
