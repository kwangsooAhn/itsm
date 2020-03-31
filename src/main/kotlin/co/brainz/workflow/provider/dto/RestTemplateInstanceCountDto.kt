package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateInstanceCountDto(
        val instanceStatus: String,
        val instanceCount: Int? = 0
) : Serializable