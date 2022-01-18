/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReportTemplate.dto

import java.io.Serializable

data class CustomReportTemplateDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val reportName: String? = null,
    val automatic: Boolean,
    var charts: List<CustomReportTemplateMapDto>? = null
) : Serializable
