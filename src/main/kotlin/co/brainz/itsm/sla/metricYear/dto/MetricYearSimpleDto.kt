/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearSimpleDto(
    val metricId: String,
    val metricYear: String? = "",
    val metricName: String? = "",
    val metricUnit: String? = ""
) : Serializable
