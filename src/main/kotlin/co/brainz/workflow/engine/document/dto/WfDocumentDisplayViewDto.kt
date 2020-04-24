package co.brainz.workflow.engine.document.dto

import java.io.Serializable

data class WfDocumentDisplayViewDto(
    var documentId: String,
    var elements: List<Map<String, Any>>? = mutableListOf(),
    var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
