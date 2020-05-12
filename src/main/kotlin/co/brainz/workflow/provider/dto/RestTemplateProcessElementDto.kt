package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateProcessElementDto(
    var process: RestTemplateProcessViewDto? = null,
    var elements: MutableList<RestTemplateElementDto>? = null
) : Serializable
