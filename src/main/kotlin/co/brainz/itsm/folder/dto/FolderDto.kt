package co.brainz.itsm.folder.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FolderDto(
    val folderId: String?,
    val instanceId: String?,
    val tokenId: String? = null,
    val relatedType: String?,
    val documentNo: String?,
    val documentName: String?,
    val documentColor: String?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?,
    val instanceStartDt: LocalDateTime?,
    val instanceEndDt: LocalDateTime?,
    val instanceStatus: String?,
    var topics: MutableList<String>? = null,
    var avatarPath: String? = null
) : Serializable
