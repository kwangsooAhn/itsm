package co.brainz.workflow.provider.dto

import java.io.Serializable

class RestTemplateInstanceDto(
        val instanceId: String,
        val processId: String,
        val instanceStatus: String? = null
) : Serializable
