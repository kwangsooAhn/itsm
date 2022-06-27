/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable

data class CalendarRequest(
    val viewType: String,
    val standard: String,
    val calendarIds: List<String>
) : Serializable
