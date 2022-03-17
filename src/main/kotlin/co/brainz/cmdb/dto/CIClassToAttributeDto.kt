/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIClassToAttributeDto(
    val classId: String? = null,
    val attributeId: String? = null,
    val attributeName: String? = null,
    val attributeType: String? = null,
    val classLevel: Int? = 1,
    val order: Int? = 1
) : Serializable
