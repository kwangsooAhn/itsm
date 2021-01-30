/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartDto(
    val chartId: String = "",
    val chartType: String = "",
    val chartName: String = "",
    val chartDesc: String? = null,
    val chartConfig: String = ""
) : Serializable
