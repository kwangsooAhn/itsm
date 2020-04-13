package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDataDto(
        val documentId: String? = "",
        val componentId: String? = "",
        val elementId: String? = "",
        val display: String? = ""
) : Serializable
