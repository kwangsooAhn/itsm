package co.brainz.workflow.provider.dto

import java.io.Serializable
import org.springframework.util.LinkedMultiValueMap

data class RestTemplateTokenViewDto(
    val token: LinkedMultiValueMap<String, String>,
    val instanceId: String,
    val form: RestTemplateFormDataDto,
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf(),
    val stakeholders: RestTemplateTokenStakeholderViewDto
) : Serializable
