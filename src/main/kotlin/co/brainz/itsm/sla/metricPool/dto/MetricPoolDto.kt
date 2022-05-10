/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricPoolDto(
    var metricId: String = "",
    var metricGroupName: String = "",
    var metricName: String = "",
    var metricDesc: String? = "",
    var metricTypeName: String = "",
    var metricUnitName: String = "",
    var calculationTypeName: String = ""
) : Serializable
