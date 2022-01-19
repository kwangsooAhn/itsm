/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import co.brainz.itsm.statistic.customChart.dto.ChartDto
import java.io.Serializable
import java.time.LocalDateTime

data class ReportDto(
    val reportId: String,
    val reportName: String,
    val reportDesc: String? = null,
    val publishDt: LocalDateTime?,
    val data: List<ChartDto> = mutableListOf()
) : Serializable
