package co.brainz.workflow.engine.document.dto

import java.io.Serializable

data class WfDocumentDisplaySaveDto(
        val documentId: String,
        val displays: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
