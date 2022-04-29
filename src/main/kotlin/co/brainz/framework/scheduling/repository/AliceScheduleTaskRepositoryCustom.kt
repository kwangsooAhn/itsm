/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchCondition

interface AliceScheduleTaskRepositoryCustom : AliceRepositoryCustom {

    fun findByScheduleList(schedulerSearchCondition: SchedulerSearchCondition): PagingReturnDto
    fun findByScheduleListByUse(): MutableList<AliceScheduleTaskEntity>
    fun findBySchedule(taskId: String): SchedulerDto?
    fun findDuplicationTaskName(taskName: String, taskId: String?): Long
}
