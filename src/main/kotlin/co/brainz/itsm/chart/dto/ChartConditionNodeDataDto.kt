package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartConditionNodeDataDto(
    var value: String? = null,
    var identifier: String? = null
) : Serializable