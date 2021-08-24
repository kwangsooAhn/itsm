/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import co.brainz.itsm.chart.dto.ChartDto
import java.io.Serializable

data class ReportTemplateDetailDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val automatic: Boolean,
    var charts: List<ChartDto>? = null
) : Serializable
