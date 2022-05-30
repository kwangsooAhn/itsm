/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricYearCopyDto(
    val metricId: String? = null,
    val source: String,
    val target: String
) : Serializable
