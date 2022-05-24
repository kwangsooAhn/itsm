/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CalendarRequest(
    val viewType: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val calendarIds: List<String>
) : Serializable
