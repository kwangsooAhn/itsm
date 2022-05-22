/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

data class CalendarResponse(
    val from: String,
    val to: String,
    val calendars: List<CalendarData>
)
