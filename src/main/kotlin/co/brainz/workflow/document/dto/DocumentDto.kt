package co.brainz.workflow.document.dto

import java.io.Serializable

data class DocumentDto(
        val documentId: String,
        val documentName: String,
        val documentDesc: String? = null
) : Serializable
