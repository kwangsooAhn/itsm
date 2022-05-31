/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.dto

import java.io.Serializable

data class DocumentStorageRequestDto(
    val instanceId: String,
    val documentId: String
) : Serializable
