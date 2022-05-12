/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.dto

import co.brainz.cmdb.constants.RestTemplateConstants
import java.io.Serializable

data class WfCIComponentValueDto(
    val ciId: String,
    val ciNo: String = "",
    val ciStatus: String = RestTemplateConstants.CIStatus.STATUS_USE.code,
    var ciIcon: String? = null,
    var ciIconData: String? = null,
    val typeId: String,
    var typeName: String? = null,
    val ciName: String? = null,
    val ciDesc: String? = null,
    var classId: String? = null,
    val interlink: Boolean = false,
    val actionType: String,
    var mappingId: String? = null
) : Serializable
