/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CIClassListDto(
    val classId: String = "",
    val className: String = "",
    val classDesc: String? = null,
    val classLevel: Int? = 1,
    val pClassId: String? = null,
    val pClassName: String? = null,
    var totalCount: Int = 0,
    var totalAttributes: Int = 0
) : Serializable
