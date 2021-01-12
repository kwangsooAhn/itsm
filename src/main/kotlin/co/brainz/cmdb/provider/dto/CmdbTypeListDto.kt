/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CmdbTypeListDto(
    val typeId: String? = null,
    val typeName: String? = null,
    val typeDesc: String? = "",
    val typeLevel: Int? = 1,
    val defaultClassId: String? = null,
    val ptypeId: String? = null,
    val typeIcon: String? = null,
    var totalCount: Long = 0
) : Serializable
