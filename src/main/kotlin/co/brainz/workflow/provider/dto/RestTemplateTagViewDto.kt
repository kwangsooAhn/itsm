package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTagViewDto(
    val id: String?,
    val value: String? = ""
) : Serializable
    