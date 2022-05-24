package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable

data class MetricManualSimpleDto(
    val metricId: String,
    val metricName: String? = ""
) : Serializable
