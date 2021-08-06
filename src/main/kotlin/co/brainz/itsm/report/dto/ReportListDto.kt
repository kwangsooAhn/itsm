/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import co.brainz.itsm.report.entity.ReportTemplateEntity
import java.io.Serializable
import java.time.LocalDateTime

data class ReportListDto(
    val reportId: String,
    val reportName: String,
    val reportDesc: String,
    val template: ReportTemplateEntity,
    val publishDt: LocalDateTime
) : Serializable
