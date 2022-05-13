/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearDto(
    var metricId: String = "",
    var metricGroupName: String = "",
    var metricName: String = "",
    var minValue: Double? = null,
    var maxValue: Double? = null,
    var weightValue: Double? = null,
    var owner: String? = null,
    var comment: String? = null
) : Serializable
