/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearExcelDto(
    val metricGroupName: String? = "",
    val metricName: String? = "",
    val minValue: Double? = 0.0,
    val maxValue: Double? = 0.0,
    val weightValue: Double? = 0.0,
    val score: Long? = 0L,
    val owner: String? = "",
    val comment: String? = ""
) : Serializable
