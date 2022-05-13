/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricDto(
    var metricId: String = "",
    var metricName: String = "",
    var metricDesc: String? = null,
    var metricGroup: String? = null,
    var metricType: String? = null,
    var metricUnit: String? = null,
    var calculationType: String? = null
) : Serializable
