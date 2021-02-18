/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.AliceScheduleTaskService
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerListDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchDto
import javax.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val aliceScheduleTaskRepository: AliceScheduleTaskRepository,
    private val aliceScheduleTaskService: AliceScheduleTaskService
) {

    /**
     * 스케줄 목록 조회.
     */
    fun getSchedulers(schedulerSearchDto: SchedulerSearchDto): List<SchedulerListDto> {
        return aliceScheduleTaskRepository.findByScheduleList(schedulerSearchDto)
    }

    /**
     * 스케줄 조회.
     */
    fun getScheduler(taskId: String): SchedulerDto? {
        return aliceScheduleTaskRepository.findBySchedule(taskId)
    }

    /**
     * 스케줄 등록.
     */
    fun insertScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = "0"
        val existCount = aliceScheduleTaskRepository.findDuplicationTaskName(
            schedulerDto.taskName,
            schedulerDto.taskId
        )
        when (existCount) {
            0L -> {
                var scheduleTaskEntity = AliceScheduleTaskEntity(
                    taskId = "",
                    taskName = schedulerDto.taskName,
                    taskDesc = schedulerDto.taskDesc,
                    useYn = schedulerDto.useYn,
                    taskType = schedulerDto.taskType,
                    executeClass = schedulerDto.executeClass,
                    executeQuery = schedulerDto.executeQuery,
                    executeCycleType = schedulerDto.executeCycleType,
                    executeCyclePeriod = schedulerDto.executeCyclePeriod,
                    cronExpression = schedulerDto.cronExpression,
                    editable = true,
                    args = schedulerDto.args
                )
                scheduleTaskEntity = aliceScheduleTaskRepository.save(scheduleTaskEntity)
                if (scheduleTaskEntity.useYn) {
                    aliceScheduleTaskService.addTaskToScheduler(scheduleTaskEntity)
                }
            }
            else -> returnValue = "2"
        }
        return returnValue
    }

    /**
     * 스케줄 수정.
     */
    fun updateScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = "0"
        var scheduleTaskEntity =
            aliceScheduleTaskRepository.findByIdOrNull(schedulerDto.taskId!!) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Schedule Entity]"
            )
        val existCount = aliceScheduleTaskRepository.findDuplicationTaskName(
            schedulerDto.taskName,
            schedulerDto.taskId
        )
        when (existCount) {
            0L -> {
                scheduleTaskEntity.taskName = schedulerDto.taskName
                scheduleTaskEntity.taskType = schedulerDto.taskType
                scheduleTaskEntity.taskDesc = schedulerDto.taskDesc
                scheduleTaskEntity.useYn = schedulerDto.useYn
                scheduleTaskEntity.executeClass = schedulerDto.executeClass
                scheduleTaskEntity.executeQuery = schedulerDto.executeQuery
                scheduleTaskEntity.executeCycleType = schedulerDto.executeCycleType
                scheduleTaskEntity.executeCyclePeriod = schedulerDto.executeCyclePeriod
                scheduleTaskEntity.cronExpression = schedulerDto.cronExpression
                scheduleTaskEntity.args = schedulerDto.args
                scheduleTaskEntity = aliceScheduleTaskRepository.save(scheduleTaskEntity)
                aliceScheduleTaskService.removeTaskFromScheduler(scheduleTaskEntity.taskId)
                if (scheduleTaskEntity.useYn) {
                    aliceScheduleTaskService.addTaskToScheduler(scheduleTaskEntity)
                }
            }
            else -> returnValue = "2"
        }
        return returnValue
    }

    /**
     * 스케줄 삭제.
     */
    @Transactional
    fun deleteScheduler(taskId: String): String {
        aliceScheduleTaskService.removeTaskFromScheduler(taskId)
        aliceScheduleTaskRepository.deleteById(taskId)
        return "0"
    }
}
