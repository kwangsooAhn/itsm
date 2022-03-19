/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReportTemplate.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CustomReportTemplateListDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val automatic: Boolean,
    val createDt: LocalDateTime? = null,
    val createUserName: String? = null,
    val chartNameList: String? = null
) : Serializable
