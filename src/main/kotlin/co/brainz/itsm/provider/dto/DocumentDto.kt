package co.brainz.itsm.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DocumentDto(
        val documentId: String,
        val documentName: String,
        val documentDesc: String? = null,
        val procId: String,
        val formId: String? = "",
        val createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable
