/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ScheduleData(
    val id: String,
    val index: Int? = 1,
    val title: String,
    val contents: String? = null,
    val allDayYn: Boolean = false,
    val startDt: LocalDateTime,
    val endDt: LocalDateTime,
    val ownerName: String? = "",
    val repeatYn: Boolean = false,
    val repeatType: String? = "",
    val repeatValue: String? = "",
    val repeatPeriod: String? = ""
) : Serializable
