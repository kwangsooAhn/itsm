/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable

data class CIDto(
    val ciId: String,
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciStatus: String? = null,
    val typeId: String,
    val classId: String? = null,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var automatic: Boolean? = false,
    var ciDataList: MutableList<CIDataDto>? = null,
    var ciRelations: MutableList<CIRelationDto>? = null,
    var ciTags: MutableList<CITagDto>? = null
) : Serializable, AliceMetaEntity()

data class CIDataDto(
    var ciId: String,
    var attributeId: String,
    var attributeData: String?
)
