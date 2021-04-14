/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIDto(
    var ciId: String,
    var ciNo: String? = null,
    var ciName: String = "",
    var ciStatus: String = "",
    val typeId: String,
    val classId: String? = null,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var automatic: Boolean? = false,
    var instanceId: String? = null,
    var ciDataList: MutableList<CIDataDto>? = null,
    var ciRelations: MutableList<CIRelationDto>? = null,
    var ciTags: MutableList<CITagDto>? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable

data class CIDataDto(
    var ciId: String,
    var attributeId: String,
    var attributeData: String?
)
