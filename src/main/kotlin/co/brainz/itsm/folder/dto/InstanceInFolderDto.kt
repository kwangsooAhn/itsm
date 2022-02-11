/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.dto

import java.io.Serializable
import java.time.LocalDateTime

data class InstanceInFolderDto(
    var folderId: String?,
    val instanceId: String?,
    val tokenId: String? = null,
    val relatedType: String?,
    val documentId: String?,
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
