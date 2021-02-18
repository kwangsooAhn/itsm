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
    var chartConfig: String? = null,
    val targetLabel: String = "",
    val operation: String = "",
    var durationDigit: Int = 0,
    val durationUnit: String = "",
    var periodUnit: String? = null,
    var group: String? = null,
    val query: String? = null
) : Serializable
