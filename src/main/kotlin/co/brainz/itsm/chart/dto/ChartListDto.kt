/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ChartListDto(
    val chartId: String? = null,
    val chartType: String? = null,
    val chartName: String? = null,
    val chartDesc: String? = "",
    val createUserName: String? = null,
    val createDt: LocalDateTime? = null,
    var totalCount: Long = 0
) : Serializable
