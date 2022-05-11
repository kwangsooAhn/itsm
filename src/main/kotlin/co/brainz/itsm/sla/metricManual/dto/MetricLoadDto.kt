package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable

data class MetricLoadDto(
    var metricId: String = "",
    var metricYear: String = "",
    var metricName: String? = "",
    var metricGroupName: String? = "",
    var metricTypeName: String? = "",
    var metricUnitName: String? = "",
    var metricCalculationTypeName: String? = ""
) : Serializable
