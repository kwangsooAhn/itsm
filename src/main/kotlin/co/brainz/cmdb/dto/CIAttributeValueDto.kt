/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIAttributeValueDto(
    val attributeId: String = "",
    val attributeName: String = "",
    val attributeDesc: String? = null,
    val attributeText: String = "",
    val attributeType: String? = null,
    val attributeOrder: Int? = 0,
    val attributeValue: String? = null,
    val searchYn: Boolean = false,
    var value: String? = null
) : Serializable

class CIAttributeValueGroupListDto(
    val attributeId: String = "",
    val attributeName: String = "",
    val attributeDesc: String? = null,
    val attributeText: String = "",
    val attributeType: String? = null,
    val attributeOrder: Int? = 0,
    val attributeValue: String? = null,
    val searchYn: Boolean = false,
    var value: String? = null,
    var childAttributes: MutableList<CIAttributeValueDto>? = mutableListOf()
) : Serializable
