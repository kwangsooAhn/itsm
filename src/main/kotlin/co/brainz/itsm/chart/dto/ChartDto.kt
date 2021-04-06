/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ChartDto(
    var chartId: String = "",
    val chartType: String = "",
    val chartName: String = "",
    val chartDesc: String? = null,
    var chartConfig: String? = null,
    var createDt: LocalDateTime? = null,
    val targetLabels: ArrayList<String>? = null,
    val operation: String = "",
    var durationDigit: Long = 0,
    val durationUnit: String = "",
    var periodUnit: String? = null,
    var group: String? = null,
    var propertyJson: String? = null
) : Serializable
