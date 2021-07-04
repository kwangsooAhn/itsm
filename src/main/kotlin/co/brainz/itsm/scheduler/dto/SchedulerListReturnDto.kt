/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable

data class SchedulerListReturnDto(
    val data: List<SchedulerListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
