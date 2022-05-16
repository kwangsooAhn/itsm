/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearDto(
    val metricId: String,
    var metricYear: String,
    var minValue: Double,
    var maxValue: Double,
    var weightValue: Double,
    var owner: String? = null,
    var comment: String? = null
) : Serializable
