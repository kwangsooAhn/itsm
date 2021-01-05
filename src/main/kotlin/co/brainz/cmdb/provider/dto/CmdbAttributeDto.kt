/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CmdbAttributeDto(
    val attributeId: String? = null,
    val attributeName: String? = null,
    val attributeDesc: String? = null,
    val attributeText: String? = null,
    val attributeType: String? = null,
    val attributeValue: String? = null,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null
) : Serializable

