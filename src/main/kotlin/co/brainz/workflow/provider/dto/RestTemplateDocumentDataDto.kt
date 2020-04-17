package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDataDto(
        val documentId: String? = "",
        val displays: MutableList<LinkedHashMap<String, Any>>
) : Serializable
