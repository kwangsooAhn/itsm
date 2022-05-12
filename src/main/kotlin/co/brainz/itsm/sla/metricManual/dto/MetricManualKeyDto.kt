package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDate

data class MetricManualKeyDto(
    var metricId: String ="",
    var referenceDt: LocalDate? = null,
    var metricValue: Double = 0.0
): Serializable
