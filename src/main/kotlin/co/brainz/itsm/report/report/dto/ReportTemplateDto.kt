/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.report.dto

import java.io.Serializable

data class ReportTemplateDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val reportName: String? = null,
    val automatic: Boolean,
    var charts: List<ReportTemplateMapDto>? = null
) : Serializable
