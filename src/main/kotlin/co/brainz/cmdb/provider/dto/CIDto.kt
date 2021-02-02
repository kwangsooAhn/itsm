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
    val classId: String?,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var automatic: Boolean? = false,
    var ciDataList: MutableList<CIDataDto>?,
    var ciRelations: MutableList<CIRelationDto>?,
    var ciTags: MutableList<CITagDto>?
) : Serializable, AliceMetaEntity()

data class CIDataDto(
    var ciId: String,
    var attributeId: String,
    var attributeData: String?
)
