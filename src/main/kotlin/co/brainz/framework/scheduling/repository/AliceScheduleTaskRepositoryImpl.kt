/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.entity.QAliceScheduleTaskEntity
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceScheduleTaskRepositoryImpl : QuerydslRepositorySupport(AliceScheduleTaskEntity::class.java),
    AliceScheduleTaskRepositoryCustom {

    override fun findByScheduleList(schedulerSearchCondition: SchedulerSearchCondition): Page<AliceScheduleTaskEntity> {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        val pageable = Pageable.unpaged()
        val query = from(schedule)
            .where(
                super.likeIgnoreCase(schedule.taskName, schedulerSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(schedule.taskType, schedulerSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(schedule.executeCycleType, schedulerSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(schedule.executeClass, schedulerSearchCondition.searchValue))
            )
            .orderBy(schedule.taskName.asc())
        val totalCount = query.fetch().size
        if (schedulerSearchCondition.isPaging) {
            query.limit(schedulerSearchCondition.contentNumPerPage)
            query.offset((schedulerSearchCondition.pageNum - 1) * schedulerSearchCondition.contentNumPerPage)
        }

        return PageImpl<AliceScheduleTaskEntity>(query.fetch(), pageable, totalCount.toLong())
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
        return query.fetch().size.toLong()
    }
}
