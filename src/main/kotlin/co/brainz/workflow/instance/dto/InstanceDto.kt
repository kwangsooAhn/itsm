package co.brainz.workflow.instance.dto

import java.io.Serializable

data class InstanceDto(
        val instanceId: String,
        val processId: String,
        val instanceStatus: String? = null
) : Serializable
