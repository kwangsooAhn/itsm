/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable

data class SchedulerListDto(
    var taskId: String,
    var taskName: String,
    var taskType: String,
    var useYn: Boolean,
    var executeClass: String? = null,
    var executeQuery: String? = null,
    var executeCycleType: String,
    var executeCyclePeriod: Long? = null,
    var cronExpression: String? = null,
    var totalCount: Long = 0
) : Serializable
