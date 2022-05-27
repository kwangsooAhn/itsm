/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricStatus.dto

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import java.io.Serializable

data class MetricStatusChartDto(
    val metricYears: String,
    val metricId: String,
    val chartType: String,
    val metricName: String? = "",
    val metricDesc: String? = "",
    val tag: MutableList<AliceTagDto>? = null,
    val chartConfig: ChartConfig,
    val chartData: MutableList<ChartData> = mutableListOf(),
    val zqlString: String? = null
) : Serializable
