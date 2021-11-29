/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartDataDto(
    val chartId: String,
    var chartType: String,
    var chartName: String,
    var chartDesc: String? = null,
    var chartConfig: String
) : Serializable
