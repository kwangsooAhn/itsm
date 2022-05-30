/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearDetailDto(
    val metricId: String,
    var metricYear: String,
    var metricGroupName: String,
    var metricName: String,
    var metricDesc: String? = null,
    var metricType: String,
    var metricUnit: String,
    var calculationType: String,
    var minValue: Double,
    var maxValue: Double,
    var weightValue: Double,
    var owner: String? = null,
    var comment: String? = null,
    var zqlString: String
) : Serializable
