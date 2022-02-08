/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.dto

import java.io.Serializable

data class InstanceFolderListDto(
    val instanceId: String,
    val documentId: String,
    val folders: List<InstanceInFolderDto>
) : Serializable
