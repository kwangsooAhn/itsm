/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.scheduling.dto.ScheduleHistoryDto
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity

interface AliceScheduleHistoryRepositoryCustom : AliceRepositoryCustom {

    fun findScheduleHistoryByTaskId(taskId: String, limit: Long?): List<AliceScheduleHistoryEntity>
    fun findScheduleLatelyHistory(): List<ScheduleHistoryDto>
}
