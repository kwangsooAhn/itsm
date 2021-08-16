/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.entity.QAliceScheduleTaskEntity
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerListDto
import co.brainz.itsm.scheduler.dto.SchedulerListReturnDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceScheduleTaskRepositoryImpl : QuerydslRepositorySupport(AliceScheduleTaskEntity::class.java),
    AliceScheduleTaskRepositoryCustom {

    override fun findByScheduleList(schedulerSearchCondition: SchedulerSearchCondition): SchedulerListReturnDto {
        val schedule = QAliceScheduleTaskEntity.aliceScheduleTaskEntity
        val query = from(schedule)
            .select(
                Projections.constructor(
                    SchedulerListDto::class.java,
                    schedule.taskId,
                    schedule.taskName,
                    schedule.taskType,
                    schedule.useYn,
                    schedule.executeClass,
                    schedule.executeQuery,
                    schedule.executeCycleType,
                    schedule.executeCyclePeriod,
                    schedule.cronExpression,
                    null, // executeTime
                    null // result
                )
            )
            .where(
                super.like(schedule.taskName, schedulerSearchCondition.searchValue)
                    ?.or(super.like(schedule.taskType, schedulerSearchCondition.searchValue))
                    ?.or(super.like(schedule.executeCycleType, schedulerSearchCondition.searchValue))
                    ?.or(super.like(schedule.executeClass, schedulerSearchCondition.searchValue))
            )
            .orderBy(schedule.taskName.asc())
            .limit(schedulerSearchCondition.contentNumPerPage)
            .offset((schedulerSearchCondition.pageNum - 1) * schedulerSearchCondition.contentNumPerPage)
            .fetchResults()

        return SchedulerListReturnDto(
            data = query.results,
            paging = AlicePagingData(
                totalCount = query.total,
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
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
