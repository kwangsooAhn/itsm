/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.dto.ScheduleHistoryDto
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.QAliceScheduleHistoryEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceScheduleHistoryRepositoryImpl : QuerydslRepositorySupport(AliceScheduleHistoryEntity::class.java),
    AliceScheduleHistoryRepositoryCustom {

    override fun findScheduleHistoryByTaskId(taskId: String, limit: Long?): List<AliceScheduleHistoryEntity> {
        val history = QAliceScheduleHistoryEntity.aliceScheduleHistoryEntity
        val query = from(history)
            .where(history.taskId.eq(taskId))
        query.orderBy(history.executeTime.desc())
        if (limit != null) {
            query.limit(limit)
        }
        return query.fetch()
    }

    override fun findScheduleLatelyHistory(): List<ScheduleHistoryDto> {
        val history = QAliceScheduleHistoryEntity.aliceScheduleHistoryEntity
        return from(history)
            .select(
                Projections.constructor(
                    ScheduleHistoryDto::class.java,
                    history.taskId,
                    history.executeTime.max(),
                    history.result
                )
            )
            .groupBy(history.taskId, history.result)
            .fetch()
    }
}
