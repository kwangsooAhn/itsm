/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

data class CalendarData(
    val id: String,
    val schedules: List<ScheduleData>,
    val repeats: List<ScheduleData>
)
