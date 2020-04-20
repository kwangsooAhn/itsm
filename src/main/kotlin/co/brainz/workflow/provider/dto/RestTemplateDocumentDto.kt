package co.brainz.workflow.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateDocumentDto(
        val documentId: String? = "",
        val documentName: String? = "",
        val documentDesc: String? = null,
        var documentStatus: String? = null,
        val procId: String ?= "",
        val formId: String? = "",
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable
