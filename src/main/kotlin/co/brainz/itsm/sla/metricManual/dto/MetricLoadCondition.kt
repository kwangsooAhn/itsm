package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricLoadCondition(
    var targetYear: String,
    var sourceYear: String? = null,
    var metricType: String? = null
) : Serializable
