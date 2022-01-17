/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import java.io.Serializable

data class CustomTemplateMapDto(
    val templateId: String,
    val chartId: String,
    val displayOrder: Int
) : Serializable
