/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CmdbClassListDto(
    var classId: String? = null,
    var className: String? = null,
    var classDesc: String? = null,
    var pClassId: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var attributes: List<CmdbClassToAttributeDto>? = null,
    var totalCount: Long = 0
) : Serializable
