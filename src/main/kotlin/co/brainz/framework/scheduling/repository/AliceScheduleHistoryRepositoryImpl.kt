/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.dto.ScheduleHistoryDto
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.QAliceScheduleHistoryEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.scheduler.dto.SchedulerHistorySearchCondition
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceScheduleHistoryRepositoryImpl : QuerydslRepositorySupport(AliceScheduleHistoryEntity::class.java),
    AliceScheduleHistoryRepositoryCustom {

    override fun findScheduleHistoryByTaskId(
        schedulerHistorySearchCondition: SchedulerHistorySearchCondition
    ): List<AliceScheduleHistoryEntity> {
        val history = QAliceScheduleHistoryEntity.aliceScheduleHistoryEntity
        val query = from(history)
            .where(history.taskId.eq(schedulerHistorySearchCondition.taskId))
            .offset(schedulerHistorySearchCondition.offset)
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
        if (schedulerHistorySearchCondition.searchResult !== null) {
            if (schedulerHistorySearchCondition.searchResult == true) {
                query.where(history.result.eq(true))
            } else {
                query.where(history.result.eq(false))
            }
        }
        query.where(
            history.executeTime.goe(schedulerHistorySearchCondition.formattedFromDt),
            history.executeTime.loe(schedulerHistorySearchCondition.formattedToDt)
        )
        query.orderBy(history.historySeq.desc())
        return query.fetch()
    }

    override fun findScheduleLatelyHistory(): List<ScheduleHistoryDto> {
        val history = QAliceScheduleHistoryEntity.aliceScheduleHistoryEntity
        return from(history)
            .select(
                Projections.constructor(
                    ScheduleHistoryDto::class.java,
                    history.taskId,
                    history.executeTime,
                    history.result
                )
            )
            .where(
                history.executeTime.`in`(
                    JPAExpressions
                        .select(history.executeTime.max())
                        .from(history)
                        .groupBy(history.taskId)
                )
            )
            .groupBy(history.taskId, history.executeTime, history.result)
            .fetch()
    }
}
