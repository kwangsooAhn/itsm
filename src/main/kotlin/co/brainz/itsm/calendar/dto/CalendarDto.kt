/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable

data class CalendarDto(
    val calendarId: String,
    val calendarType: String,
    val calendarName: String? = "",
    val owner: String? = null
) : Serializable
