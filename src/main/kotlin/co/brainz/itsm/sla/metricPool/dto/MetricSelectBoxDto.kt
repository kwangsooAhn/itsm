package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricSelectBoxDto(
    val metricId: String,
    val metricName: String? = ""
) : Serializable
