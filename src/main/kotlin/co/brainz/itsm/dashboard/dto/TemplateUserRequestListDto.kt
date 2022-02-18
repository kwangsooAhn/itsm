/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class TemplateUserRequestListDto(
    var documentId: String,
    var documentName: String,
    var count: Long = 0L
) : Serializable
