/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ChartDateTimeDto(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime?,
    val endYear: Int,
    val endMonth: Int,
    val endDays: Int,
    val endHours: Int,
    var startYear: Int,
    var startMonth: Int,
    var startDays: Int,
    var startHours: Int,
    val period: Int
) : Serializable
