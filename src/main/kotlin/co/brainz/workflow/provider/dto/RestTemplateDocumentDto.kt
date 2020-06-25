package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateDocumentDto(
    val documentId: String = "",
    val documentType: String = "",
    val documentName: String = "",
    val documentDesc: String? = null,
    var documentStatus: String? = null,
    val processId: String = "",
    val formId: String = "",
    val documentNumberingRuleId: String = "",
    val documentColor: String? = "",
    var documentGroup: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
