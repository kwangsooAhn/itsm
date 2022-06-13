/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CalendarDocument(
    val id: String,
    val title: String,
    val contents: String? = null,
    val startDt: LocalDateTime,
    val endDt: LocalDateTime,
    val allDayYn: Boolean
) : Serializable
