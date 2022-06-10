/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable

data class CalendarData(
    val id: String,
    val type: String,
    val schedules: List<ScheduleData> = emptyList(),
    val repeats: List<ScheduleData> = emptyList()
) : Serializable
