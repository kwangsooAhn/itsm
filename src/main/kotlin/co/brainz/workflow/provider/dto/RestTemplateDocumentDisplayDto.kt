package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDisplayDto(
    val documentId: String? = "",
    val displays: MutableList<LinkedHashMap<String, Any>>
) : Serializable
