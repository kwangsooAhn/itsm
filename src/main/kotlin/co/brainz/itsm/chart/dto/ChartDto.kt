/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import com.google.gson.JsonArray
import java.io.Serializable
import java.time.LocalDateTime

data class ChartDto(
    val chartId: String = "",
    val chartType: String = "",
    val chartName: String = "",
    val chartDesc: String? = null,
    var chartConfig: String? = null,
    val createDt: LocalDateTime? = null,
    val targetLabel: String = "",
    val operation: String = "",
    var durationDigit: Long = 0,
    val durationUnit: String = "",
    var periodUnit: String? = null,
    var group: String? = null,
    var query: JsonArray? = null
) : Serializable
