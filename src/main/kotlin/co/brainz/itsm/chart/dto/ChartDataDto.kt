package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartDataDto(
    val chartId: String,
    var chartType: String,
    var chartName: String,
    var chartDesc: String? = null,
    var chartConfig: String
) : Serializable
