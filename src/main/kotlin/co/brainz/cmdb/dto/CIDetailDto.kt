/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable
import java.time.LocalDateTime

data class CIDetailDto(
    var ciId: String,
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciIcon: String? = null,
    var ciIconData: String? = null,
    var ciDesc: String? = null,
    var ciStatus: String? = null,
    var interlink: Boolean? = false,
    var typeId: String? = null,
    var typeName: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var classes: List<CIClassDetailValueForGroupListDto>? = null,
    var ciRelations: MutableList<CIRelationDto>? = null,
    var ciTags: List<AliceTagDto>? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable

class CIClassDetailValueDto(
    var attributes: MutableList<CIAttributeValueDto>? = null
) : Serializable

class CIClassDetailValueForGroupListDto(
    var attributes: MutableList<CIAttributeValueGroupListDto>? = null
) : Serializable
