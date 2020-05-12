package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentDisplaySaveDto(
    val documentId: String,
    val displays: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
