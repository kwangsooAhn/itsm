package co.brainz.itsm.sla.metricStatus.dto

import java.io.Serializable

data class MetricAnnualDto(
    val metricId: String = "",
    val metricGroupName: String? = "",
    val metricName: String? = "",
    val minValue: Double? = 0.0,
    val maxValue: Double? = 0.0,
    val weightValue: Int = 0,
    var score: Double? = 0.0,
    val owner: String? = "",
    val comment: String? = ""
) : Serializable
