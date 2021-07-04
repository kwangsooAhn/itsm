/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.entity.QAliceScheduleTaskEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceScheduleTaskRepositoryImpl : QuerydslRepositorySupport(AliceScheduleTaskEntity::class.java),
    AliceScheduleTaskRepositoryCustom {

    override fun findByScheduleList(schedulerSearchDto: SchedulerSearchDto): QueryResults<AliceScheduleTaskEntity>? {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        return from(schedule)
            .where(
                super.like(schedule.taskName, schedulerSearchDto.search)
                    ?.or(super.like(schedule.taskType, schedulerSearchDto.search))
                    ?.or(super.like(schedule.executeCycleType, schedulerSearchDto.search))
                    ?.or(super.like(schedule.executeClass, schedulerSearchDto.search))
            )
            .orderBy(schedule.taskName.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT).offset(schedulerSearchDto.offset)
            .fetchResults()
    }

    override fun findByScheduleListByUse(): MutableList<AliceScheduleTaskEntity> {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        return from(schedule)
            .where(schedule.useYn.eq(true))
            .fetch()
    }

    override fun findBySchedule(taskId: String): SchedulerDto? {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        return from(schedule)
            .select(
                Projections.constructor(
                    SchedulerDto::class.java,
                    schedule.taskId,
                    schedule.taskName,
                    schedule.taskDesc,
                    schedule.taskType,
                    schedule.useYn,
                    schedule.executeClass,
                    schedule.executeQuery,
                    schedule.executeCommand,
                    schedule.executeCycleType,
                    schedule.executeCyclePeriod,
                    schedule.cronExpression,
                    schedule.editable,
                    schedule.args,
                    schedule.src
                )
            )
            .where(schedule.taskId.eq(taskId))
            .fetchOne()
    }

    /**
     * Task Name 중복체크.
     */
    override fun findDuplicationTaskName(taskName: String, taskId: String?): Long {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        val query = from(schedule)
            .where(schedule.taskName.eq(taskName))
        if (taskId != null) {
            query.where(!schedule.taskId.eq(taskId))
        }
        return query.fetchCount()
    }
}
