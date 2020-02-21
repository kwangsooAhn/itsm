package co.brainz.workflow.document.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DocumentDto(
        val documentId: String,
        val documentName: String,
        val documentDesc: String? = null,
        val procId: String,
        val formId: String? = "",
        val createUserKey: String? = null,
        val createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        val updateDt: LocalDateTime? = null
) : Serializable
