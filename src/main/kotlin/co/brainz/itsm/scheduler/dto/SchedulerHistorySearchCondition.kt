/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable

data class SchedulerHistorySearchCondition(
    var taskId: String = "",
    var offset: Long,
    var isScroll: Boolean = false
) : Serializable
