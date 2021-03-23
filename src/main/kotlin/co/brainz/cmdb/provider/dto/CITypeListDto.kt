/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CITypeListDto(
    val typeId: String? = null,
    val typeName: String? = null,
    val typeDesc: String? = null,
    val typeLevel: Int? = 1,
    val typeAlias: String? = null,
    var pTypeId: String? = null,
    var pTypeName: String? = null,
    val typeIcon: String? = null,
    val defaultClassId: String? = null,
    val defaultClassName: String? = null
) : Serializable
