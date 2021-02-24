/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable

data class SchedulerDto(
    var taskId: String?,
    var taskName: String,
    var taskDesc: String?,
    var taskType: String,
    var useYn: Boolean,
    var executeClass: String?,
    var executeQuery: String?,
    var executeCycleType: String,
    var executeCyclePeriod: Long?,
    var cronExpression: String?,
    var editable: Boolean?,
    var args: String?
) : Serializable
