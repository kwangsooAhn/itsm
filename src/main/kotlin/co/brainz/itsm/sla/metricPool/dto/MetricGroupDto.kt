/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricGroupDto(
    var metricGroupId: String = "",
    var metricGroupName: String = ""
) : Serializable
