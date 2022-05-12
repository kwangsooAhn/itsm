package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricLoadCondition(
    var sourceYear: String = "",
    var targetYear: String? = "",
    var metricType: String? = ""
) : Serializable
