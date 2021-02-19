/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.dto

import java.time.LocalDateTime

data class ScheduleHistoryDto(
    var taskId: String?,
    var executeTime: LocalDateTime?,
    var result: Boolean?
)
