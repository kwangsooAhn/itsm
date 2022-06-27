/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class InstanceScheduleDto(
    val instanceId: String,
    val documentId: String,
    var title: String,
    var contents: String? = null,
    val allDayYn: Boolean = false,
    var startDt: LocalDateTime,
    var endDt: LocalDateTime
) : Serializable
