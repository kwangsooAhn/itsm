/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIGroupListDataDto(
    val ciId: String,
    val attributeId: String,
    val cAttributeId: String,
    val cAttributeSeq: Int,
    val cValue: String?,
    val cAttributeText: String
) : Serializable
