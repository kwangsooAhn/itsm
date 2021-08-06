/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ReportTemplateListDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String,
    val automatic: Boolean,
    val createDt: LocalDateTime,
    val createUserName: String
) : Serializable
