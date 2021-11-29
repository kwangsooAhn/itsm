/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceScheduleHistoryRepository : CrudRepository<AliceScheduleHistoryEntity, String>,
    AliceScheduleHistoryRepositoryCustom {

    @Query("SELECT COUNT(s.taskId) FROM AliceScheduleHistoryEntity s WHERE s.taskId = :taskId")
    fun countByScheduleHistoryByTaskId(taskId: String): Long

}
