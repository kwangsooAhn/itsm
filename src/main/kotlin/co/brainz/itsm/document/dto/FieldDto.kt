/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import java.io.Serializable

data class FieldDto(
    val name: String,
    val alias: String,
    val width: Int
) : Serializable
