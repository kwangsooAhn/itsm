/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CITypeTreeListDto(
    val typeId: String? = null,
    val typeName: String? = null,
    val typeDesc: String? = null,
    val typeLevel: Int? = 1,
    val typeSeq: Int? = 0,
    val typeAlias: String? = null,
    var pTypeId: String? = null,
    var pTypeName: String? = null,
    val typeIcon: String? = null,
    val typeIconData: String? = null,
    val classId: String? = null,
    val className: String? = null
) : Serializable
