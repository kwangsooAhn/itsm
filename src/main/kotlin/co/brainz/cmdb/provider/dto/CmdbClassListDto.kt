/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CmdbClassListDto(
    var classId: String = "",
    var className: String = "",
    var classDesc: String? = null,
    var pClassId: String? = null
) : Serializable
