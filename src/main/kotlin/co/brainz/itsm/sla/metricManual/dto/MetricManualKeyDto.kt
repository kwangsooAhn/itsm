package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricManualKeyDto(
    var metricId: String ="",
    var referenceDt: LocalDateTime,
    var metricValue: Int = 0
): Serializable
