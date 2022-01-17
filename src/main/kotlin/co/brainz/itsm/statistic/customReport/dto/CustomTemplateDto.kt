/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import java.io.Serializable

data class CustomTemplateDto(
    val templateId: String,
    val templateName: String,
    val templateDesc: String? = null,
    val reportName: String? = null,
    val automatic: Boolean,
    var charts: List<CustomTemplateMapDto>? = null
) : Serializable
