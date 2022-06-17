/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.dto

import java.io.Serializable

data class DocumentStorageDto(
    val instanceId: String,
    val userKey: String
) : Serializable
