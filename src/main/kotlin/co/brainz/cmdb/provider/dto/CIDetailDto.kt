/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIDetailDto(
    var ciId: String? = null,
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var ciStatus: String? = null,
    var automatic: Boolean? = false,
    var typeId: String? = null,
    var typeName: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var classes: MutableList<CIClassDetailValueDto>? = null,
    var ciRelations: MutableList<CIRelationDto>? = null,
    var ciTags: MutableList<CITagDto>? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable

class CIClassDetailValueDto(
    var attributes: MutableList<CIAttributeValueDto>? = null
) : Serializable
