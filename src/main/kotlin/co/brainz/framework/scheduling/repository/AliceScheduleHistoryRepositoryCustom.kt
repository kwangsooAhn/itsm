/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.scheduling.dto.ScheduleHistoryDto
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.itsm.scheduler.dto.SchedulerHistorySearchCondition

interface AliceScheduleHistoryRepositoryCustom : AliceRepositoryCustom {

    fun findScheduleHistoryByTaskId(
        schedulerHistorySearchCondition: SchedulerHistorySearchCondition
    ): List<AliceScheduleHistoryEntity>

    fun findScheduleLatelyHistory(): List<ScheduleHistoryDto>
}
