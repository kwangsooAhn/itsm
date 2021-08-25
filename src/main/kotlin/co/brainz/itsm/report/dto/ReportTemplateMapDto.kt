/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import java.io.Serializable

data class ReportTemplateMapDto(
    val templateId: String,
    val chartId: String,
    val displayOrder: Int
) : Serializable
