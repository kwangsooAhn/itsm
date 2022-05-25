package co.brainz.itsm.sla.metricStatus.dto

import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import java.io.Serializable

data class MetricStatusChartDto(
    val metricYears: String,
    val metricId: String,
    val chartType: String,
    val chartData: MutableList<ChartData> = mutableListOf(),
    val tag: String? = null,
    val chartConfig: ChartConfig,
    val zqlString: String? = null
):Serializable
