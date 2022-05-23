/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearExcelDto(
    val metricGroupName: String?,
    val metricName: String?,
    val minValue: Double?,
    val maxValue: Double?,
    val weightValue: Double?,
    val score: Long?,
    val owner: String?,
    val comment: String?
) : Serializable
