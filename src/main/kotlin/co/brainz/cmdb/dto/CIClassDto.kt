/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIClassDto(
    var classId: String = "",
    var className: String = "",
    var classDesc: String? = null,
    val classLevel: Int? = 1,
    val classSeq: Int? = 0,
    var pClassId: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var attributes: List<String>? = null
) : Serializable
