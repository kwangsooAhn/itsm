/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable

data class ChartDto(
    var chartId: String = "",
    var chartName: String = "",
    var chartType: String = "",
    var chartDesc: String? = null,
    var tags: List<AliceTagDto> = emptyList(),
    var chartConfig: ChartConfig,
    var chartData: MutableList<ChartData> = mutableListOf()
) : Serializable
