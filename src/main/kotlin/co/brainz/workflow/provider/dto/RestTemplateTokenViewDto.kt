package co.brainz.workflow.provider.dto

import org.springframework.util.LinkedMultiValueMap
import java.io.Serializable

data class RestTemplateTokenViewDto(
    val token: LinkedMultiValueMap<String, String>,
    val instanceId: String,
    val form: RestTemplateFormComponentListDto,
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf(),
    val stakeholders: RestTemplateTokenStakeholderViewDto
) : Serializable
