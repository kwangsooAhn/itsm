/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable

data class CommentDto(
    val documentId: String?,
    val commentValue: String
) : Serializable
