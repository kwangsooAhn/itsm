/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CIAttributeValueDto(
        val attributeId: String = "",
        val attributeName: String = "",
        val attributeText: String = "",
        val attributeType: String? = null,
        val attributeOrder: Int? = 0,
        var attributeValue: String? = null,
        val value: String? = null
) : Serializable
