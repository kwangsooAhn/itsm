/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricLoadCondition(
    var source: String = "",
    var target: String? = "",
    var type: String? = ""
) : Serializable
