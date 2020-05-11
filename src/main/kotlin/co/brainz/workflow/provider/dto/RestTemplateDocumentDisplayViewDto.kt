package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDisplayViewDto(
    var documentId: String,
    var elements: List<Map<String, Any>>? = mutableListOf(),
    var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
