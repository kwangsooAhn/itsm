/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceScheduleTaskRepository : CrudRepository<AliceScheduleTaskEntity, String>,
    AliceScheduleTaskRepositoryCustom {
    override fun findAll(): MutableList<AliceScheduleTaskEntity>
}
