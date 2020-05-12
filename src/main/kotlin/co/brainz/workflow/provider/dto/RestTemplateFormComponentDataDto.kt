package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateFormComponentDataDto(
    val componentId: String = "",
    val attributeId: String = "",
    val attributeValue: String = ""
) : Serializable
