/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CustomReportListDto(
    val reportId: String,
    val reportName: String,
    val reportDesc: String,
    val templateId: String,
    val publishDt: LocalDateTime
) : Serializable
