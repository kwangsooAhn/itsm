/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

data class CmdbClassDetailDto(
    var classId: String = "",
    var className: String = "",
    var classDesc: String? = null,
    var pClassId: String? = null,
    var pClassName: String? = null,
    var attributes: List<CmdbClassToAttributeDto>? = null
)
