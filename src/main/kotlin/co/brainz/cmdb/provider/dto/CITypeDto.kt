/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CITypeDto(
    val typeId: String = "",
    val typeName: String? = "",
    val typeDesc: String? = null,
    val typeLevel: Int? = 1,
    val typeAlias: String? = "",
    var pTypeId: String? = "",
    var pTypeName: String? = "",
    val typeIcon: String? = null,
    var defaultClassId: String = "",
    val defaultClassName: String? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
