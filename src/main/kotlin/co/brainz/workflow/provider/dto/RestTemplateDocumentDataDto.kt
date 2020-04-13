package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDataDto(
        val documentId: String? = "",
        val components: String? = "",//List<RestTemplateComponentDataDto>,
        val elements: String? = "",//List<Map<String, Any>>,
        val displays: String? = ""//MutableList<RestTemplateDocumentDisplayDto>
) : Serializable
