/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceScheduleHistoryRepository : CrudRepository<AliceScheduleHistoryEntity, String>,
    AliceScheduleHistoryRepositoryCustom {

    fun countScheduleHistoryByTaskId(taskId: String): Long
}
