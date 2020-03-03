package co.brainz.itsm.provider.dto

import java.io.Serializable

class InstanceDto(
        val instanceId: String,
        val processId: String,
        val instanceStatus: String? = null
) : Serializable
