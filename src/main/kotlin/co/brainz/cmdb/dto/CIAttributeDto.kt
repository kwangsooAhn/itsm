/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.*
import java.time.*

data class CIAttributeDto(
    val attributeId: String = "",
    val attributeName: String = "",
    val attributeDesc: String? = null,
    val attributeText: String = "",
    val attributeType: String? = null,
    val attributeValue: String? = null,
    val searchYn: Boolean = false,
    val mappingId: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var enabled: Boolean = true
) : Serializable
