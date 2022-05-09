/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricPoolListDto(
    var metricId: String = "",
    var metricGroupName: String = "",
    var metricName: String = "",
    var metricType: String = "",
    var metricUnit: String = "",
    var calculationType: String = "",
    var metricDesc: String? = ""
) : Serializable
