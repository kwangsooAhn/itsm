/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.dto

import java.io.Serializable

data class FolderMappingDto(
    val folderId: String,
    val orgInstanceId: String,
    val relInstanceId: String
) : Serializable
