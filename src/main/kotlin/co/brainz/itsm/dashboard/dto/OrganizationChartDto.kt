/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import java.io.Serializable

data class OrganizationChartDto(
    val chartId: String,
    val chartName: String?,
    val chartType: String,
    val chartDesc: String? = "",
    val tags: MutableList<AliceTagDto>,
    val chartConfig: List<ChartConfig>,
    val chartData: MutableList<ChartData>
) : Serializable
