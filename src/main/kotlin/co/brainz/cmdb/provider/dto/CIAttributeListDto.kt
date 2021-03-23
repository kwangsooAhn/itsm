/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CIAttributeListDto(
    val attributeId: String? = null,
    val attributeName: String? = null,
    val attributeText: String? = "",
    val attributeType: String? = null,
    val attributeDesc: String? = null
) : Serializable
