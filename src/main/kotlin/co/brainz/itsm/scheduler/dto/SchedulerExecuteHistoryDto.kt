/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable
import java.time.LocalDateTime

data class SchedulerExecuteHistoryDto(
    var minExecuteTime: LocalDateTime,
    var maxExecuteTime: LocalDateTime
) : Serializable
