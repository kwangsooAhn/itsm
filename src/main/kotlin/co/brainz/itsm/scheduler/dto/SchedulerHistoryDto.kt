/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable
import java.time.LocalDateTime

data class SchedulerHistoryDto(
    var historySeq: Long = 0,
    val taskId: String,
    var immediatelyExecute: Boolean = false,
    var executeTime: LocalDateTime? = LocalDateTime.now(),
    var result: Boolean? = true,
    var errorMessage: String? = null
) : Serializable
