package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateFormComponentSaveDto(
        val form: RestTemplateFormSaveDto,
        var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
