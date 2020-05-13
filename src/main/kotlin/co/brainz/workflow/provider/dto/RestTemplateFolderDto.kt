package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateFolderDto(
    val folderId: String?,
    val instanceId: String?,
    val documentName: String?,
    val instanceStartDt: LocalDateTime?,
    val instanceEndDt: LocalDateTime?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?
) : Serializable
