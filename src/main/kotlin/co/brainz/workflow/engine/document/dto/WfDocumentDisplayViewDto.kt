package co.brainz.workflow.engine.document.dto

import java.io.Serializable

data class WfDocumentDisplayViewDto(
        val documentId: String,
        val components:  MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
