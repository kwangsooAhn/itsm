package co.brainz.workflow.engine.document.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfDocumentDto(
        val documentId: String,
        val documentName: String,
        val documentDesc: String? = null,
        val procId: String,
        val formId: String,
        val createUserKey: String? = null,
        val createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        val updateDt: LocalDateTime? = null
) : Serializable
