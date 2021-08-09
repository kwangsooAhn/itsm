/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import java.io.Serializable

data class ReportTemplateDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String,
    val automatic: Boolean
) : Serializable
