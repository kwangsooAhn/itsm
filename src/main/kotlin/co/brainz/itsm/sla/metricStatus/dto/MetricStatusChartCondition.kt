/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricStatus.dto

import java.io.Serializable

data class MetricStatusChartCondition(
    val metricId: String,
    val year: String,
    val chartType: String
) : Serializable
