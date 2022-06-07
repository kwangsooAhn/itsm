/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearDto(
    val metricId: String,
    var year: String,
    var minValue: Float,
    var maxValue: Float,
    var weightValue: Float,
    var owner: String? = null,
    var comment: String? = null,
    var zqlString: String
) : Serializable
