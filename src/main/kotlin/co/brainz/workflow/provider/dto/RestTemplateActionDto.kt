package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateActionDto(
    val name: String,
    val value: String
) : Serializable
