/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import co.brainz.itsm.statistic.customChart.dto.ChartDto
import java.io.Serializable
import java.time.LocalDateTime

data class CustomTemplateListDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val automatic: Boolean,
    val charts: List<ChartDto>? = null,
    val createDt: LocalDateTime? = null,
    val createUserName: String? = null
) : Serializable
