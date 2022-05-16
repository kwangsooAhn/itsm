/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricDto(
    val metricId: String,
    var metricName: String,
    var metricDesc: String? = null,
    var metricGroup: String,
    var metricType: String,
    var metricUnit: String,
    var calculationType: String
) : Serializable
