/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class SchedulerListReturnDto(
    val data: List<SchedulerListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
