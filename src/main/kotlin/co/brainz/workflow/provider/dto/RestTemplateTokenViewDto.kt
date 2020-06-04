package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenViewDto(
    val tokenId: String,
    val instanceId: String,
    val form: RestTemplateFormComponentListDto,
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf()
) : Serializable
