package co.brainz.workflow.engine.document.dto

import java.io.Serializable

data class WfDocumentDataDto(
        val documentId: String,
        val componentId: String,
        val elementId: String,
        val display: String
) : Serializable
