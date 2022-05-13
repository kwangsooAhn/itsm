/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricLoadCondition(
    var sourceYear: String = "",
    var targetYear: String? = "",
    var metricType: String? = ""
) : Serializable
