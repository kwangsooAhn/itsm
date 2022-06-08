/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CalendarDeleteRequest(
    val id: String,
    val dataId: String? = null,
    val standard: String,
    val index: Int = 1,
    val repeatYn: Boolean? = false,
    val repeatPeriod: String? = null,
    val startDt: LocalDateTime? = null
) : Serializable
