/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CmdbTypeDto(
    val typeId: String? = null,
    val typeName: String? = null,
    val typeDesc: String? = null,
    val typeLevel: Int? = 1,
    val defaultClassId: String? = null,
    val pTypeId: String? = null,
    val typeIcon: String? = null,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null
) : Serializable
