package co.brainz.itsm.folder.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FolderDto(
    val folderId: String?,
    val instanceId: String,
    val relatedType: String?,
    val tokenId: String? = null,
    val documentNo: String?,
    val documentName: String?,
    var createUserKey: String?,
    val createDt: LocalDateTime? = null,
    val instanceStartDt: LocalDateTime?,
    val instanceEndDt: LocalDateTime?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?
) : Serializable
