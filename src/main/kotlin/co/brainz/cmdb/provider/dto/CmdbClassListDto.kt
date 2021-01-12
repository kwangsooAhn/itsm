/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CmdbClassListDto(
    val classId: String? = null,
    val className: String? = null,
    val classDesc: String? = null,
    val pClassId: String? = null,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null,
    val attributes: List<CmdbClassToAttributeDto>? = null
) : Serializable
