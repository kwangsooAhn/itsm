/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.service

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.AliceScheduleTaskService
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerHistoryDto
import co.brainz.itsm.scheduler.dto.SchedulerListDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchDto
import java.time.Instant
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val aliceScheduleTaskRepository: AliceScheduleTaskRepository,
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository,
    private val aliceScheduleTaskService: AliceScheduleTaskService,
    private val scheduler: TaskScheduler
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 스케줄 목록 조회.
     */
    fun getSchedulers(schedulerSearchDto: SchedulerSearchDto): List<SchedulerListDto> {
        val latelyHistory = aliceScheduleHistoryRepository.findScheduleLatelyHistory()
        val schedulers = aliceScheduleTaskRepository.findByScheduleList(schedulerSearchDto)
        val schedulerList = mutableListOf<SchedulerListDto>()
        if (schedulers != null) {
            for (scheduler in schedulers.results) {
                val schedulerDto = SchedulerListDto(
                    taskId = scheduler.taskId,
                    taskName = scheduler.taskName,
                    taskType = scheduler.taskType,
                    useYn = scheduler.useYn,
                    executeClass = scheduler.executeClass,
                    executeQuery = scheduler.executeQuery,
                    executeCycleType = scheduler.executeCycleType,
                    executeCyclePeriod = scheduler.executeCyclePeriod,
                    cronExpression = scheduler.cronExpression,
                    totalCount = schedulers.total
                )
                latelyHistory.forEach { history ->
                    if (history.taskId == scheduler.taskId) {
                        schedulerDto.executeTime = history.executeTime
                        schedulerDto.result = history.result
                    }
                }
                schedulerList.add(schedulerDto)
            }
        }
        return schedulerList
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

    /**
     * 스케줄 즉시 실행.
     */
    fun executeScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = "0"
        val history = SchedulerHistoryDto(
            taskId = schedulerDto.taskId!!,
            immediatelyExecute = true
        )
        when (schedulerDto.taskType) {
            AliceConstants.ScheduleTaskType.QUERY.code -> {
                schedulerDto.executeQuery?.let { aliceScheduleTaskService.executeQuery(it) }
            }
            AliceConstants.ScheduleTaskType.CLASS.code -> {
                try {
                    val taskClass = Class.forName(schedulerDto.executeClass)
                        .asSubclass(Runnable::class.java)
                    val args = aliceScheduleTaskService.getArgs(schedulerDto.args)
                    val runnable: Runnable
                    runnable = if (args.isEmpty()) {
                        taskClass.getDeclaredConstructor().newInstance()
                    } else {
                        taskClass.getDeclaredConstructor(Any::class.java).newInstance(args)
                    }
                    scheduler.schedule(runnable, Instant.now())
                    history.result = true
                } catch (e: Exception) {
                    logger.error("Failed to load class. [{}]", schedulerDto.executeClass)
                    e.printStackTrace()
                    returnValue = "3"
                    history.result = false
                    history.errorMessage = AliceUtil().printStackTraceToString(e)
                }
            }
        }
        this.setSchedulerHistory(history)
        return returnValue
    }

    /**
     * 스케줄 이력 등록.
     */
    fun setSchedulerHistory(history: SchedulerHistoryDto) {
        val historyEntity = AliceScheduleHistoryEntity(
            historySeq = 0L,
            taskId = history.taskId,
            immediatelyExecute = history.immediatelyExecute,
            result = history.result,
            executeTime = LocalDateTime.now(),
            errorMessage = history.errorMessage
        )
        aliceScheduleHistoryRepository.save(historyEntity)
    }

    fun getSchedulerHistory(taskId: String): List<AliceScheduleHistoryEntity> {
        return aliceScheduleHistoryRepository.findScheduleHistoryByTaskId(taskId, ItsmConstants.SEARCH_DATA_COUNT)
    }
}