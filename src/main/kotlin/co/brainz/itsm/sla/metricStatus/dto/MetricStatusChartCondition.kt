package co.brainz.itsm.sla.metricStatus.dto

import java.io.Serializable

data class MetricStatusChartCondition(
    val metricId: String,
    val year: String,
    val metricName: String? = "",
    val chartType: String
):Serializable
