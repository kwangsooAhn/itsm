/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class ChartRange(
    var type: String,
    var fromDate: LocalDate? = null,
    var toDate: LocalDate? = null
) : Serializable {
    val fromDateTime: LocalDateTime? = fromDate?.atTime(0,0,0)
    val toDateTime: LocalDateTime? = toDate?.atTime(23,59,59)
}
