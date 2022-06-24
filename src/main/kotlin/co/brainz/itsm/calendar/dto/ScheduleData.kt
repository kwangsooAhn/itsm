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
    val dataId: String? = null,
    val index: Int? = 1,
    var title: String,
    var contents: String? = null,
    val allDayYn: Boolean = false,
    var startDt: LocalDateTime,
    var endDt: LocalDateTime,
    val ownerName: String? = "",
    val repeatYn: Boolean = false,
    val repeatType: String? = "",
    val repeatValue: String? = "",
    val repeatPeriod: String? = "",
    val instanceId: String? = null
) : Serializable
