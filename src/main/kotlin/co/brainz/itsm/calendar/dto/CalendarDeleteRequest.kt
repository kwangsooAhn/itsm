/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable

data class CalendarDeleteRequest(
    val id: String,
    val dataId: String? = null,
    val repeatYn: Boolean? = false,
    val repeatPeriod: String? = null
) : Serializable
