/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.service

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.AliceScheduleTaskService
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeClass
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeJar
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeQuery
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.scheduler.constants.SchedulerConstants
import co.brainz.itsm.scheduler.dto.SchedulerListDto
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerHistorySearchCondition
import co.brainz.itsm.scheduler.dto.SchedulerListReturnDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchCondition
import java.io.File
import java.nio.file.Paths
import java.time.Instant
import javax.transaction.Transactional
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val aliceScheduleTaskRepository: AliceScheduleTaskRepository,
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository,
    private val aliceScheduleTaskService: AliceScheduleTaskService,
    private val scheduler: TaskScheduler,
    private val scheduleTaskTypeQuery: ScheduleTaskTypeQuery,
    private val scheduleTaskTypeClass: ScheduleTaskTypeClass,
    private val scheduleTaskTypeJar: ScheduleTaskTypeJar
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${schedule.plugins.dir}")
    private val pluginsDir: String? = null

    /**
     * 스케줄 목록 조회.
     */
    fun getSchedulers(schedulerSearchCondition: SchedulerSearchCondition): SchedulerListReturnDto {
        val latelyHistory = aliceScheduleHistoryRepository.findScheduleLatelyHistory()
        val queryResult = aliceScheduleTaskRepository.findByScheduleList(schedulerSearchCondition)
        val schedulerList = mutableListOf<SchedulerListDto>()

        for (scheduler in queryResult.results) {
            val schedulerDto = SchedulerListDto(
                taskId = scheduler.taskId,
                taskName = scheduler.taskName,
                taskType = scheduler.taskType,
                useYn = scheduler.useYn,
                executeClass = scheduler.executeClass,
                executeQuery = scheduler.executeQuery,
                executeCycleType = scheduler.executeCycleType,
                executeCyclePeriod = scheduler.executeCyclePeriod,
                cronExpression = scheduler.cronExpression
            )
            latelyHistory.forEach { history ->
                if (history.taskId == scheduler.taskId) {
                    schedulerDto.executeTime = history.executeTime
                    schedulerDto.result = history.result
                }
            }
            schedulerList.add(schedulerDto)
        }

        return SchedulerListReturnDto(
            data = schedulerList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = aliceScheduleTaskRepository.count(),
                currentPageNum = schedulerSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 스케줄 조회.
     */
    fun getSchedulerDetail(taskId: String): SchedulerDto? {
        return aliceScheduleTaskRepository.findBySchedule(taskId)
    }

    /**
     * 스케줄 등록.
     */
    fun insertScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = SchedulerConstants.Status.STATUS_SUCCESS.code
        val existCount = aliceScheduleTaskRepository.findDuplicationTaskName(
            schedulerDto.taskName,
            schedulerDto.taskId
        )

        when (schedulerDto.taskType) {
            SchedulerConstants.Types.JAR.type -> {
                val fileExist = schedulerDto.executeCommand?.let { executeCommand ->
                    schedulerDto.src?.let { src -> this.validateJarFile(src, executeCommand) }
                } ?: false
                if (!fileExist) {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST.code
                    return returnValue
                }
            }
            SchedulerConstants.Types.CLASS.type -> {
                val fileExist = schedulerDto.executeClass?.let { executeClass ->
                    this.validateClassFile(executeClass)
                } ?: false
                if (!fileExist) {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_CLASS_NOT_EXIST.code
                    return returnValue
                }
            }
        }

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
                    executeCommand = schedulerDto.executeCommand,
                    executeCycleType = schedulerDto.executeCycleType,
                    executeCyclePeriod = schedulerDto.executeCyclePeriod,
                    cronExpression = schedulerDto.cronExpression,
                    editable = true,
                    args = schedulerDto.args,
                    src = schedulerDto.src
                )
                scheduleTaskEntity = aliceScheduleTaskRepository.save(scheduleTaskEntity)
                if (scheduleTaskEntity.useYn) {
                    aliceScheduleTaskService.addTaskToScheduler(scheduleTaskEntity)
                }
            }
            else -> returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_NAME_DUPLICATION.code
        }
        return returnValue
    }

    /**
     * 스케줄 수정.
     */
    fun updateScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = SchedulerConstants.Status.STATUS_SUCCESS.code
        var scheduleTaskEntity =
            aliceScheduleTaskRepository.findByIdOrNull(schedulerDto.taskId!!) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Schedule Entity]"
            )
        val existCount = aliceScheduleTaskRepository.findDuplicationTaskName(
            schedulerDto.taskName,
            schedulerDto.taskId
        )
        when (schedulerDto.taskType) {
            SchedulerConstants.Types.JAR.type -> {
                val fileExist = schedulerDto.executeCommand?.let { executeCommand ->
                    schedulerDto.src?.let { src -> this.validateJarFile(src, executeCommand) }
                } ?: false
                if (!fileExist) {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST.code
                    return returnValue
                }
            }
            SchedulerConstants.Types.CLASS.type -> {
                val fileExist = schedulerDto.executeClass?.let { executeClass ->
                    this.validateClassFile(executeClass)
                } ?: false
                if (!fileExist) {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_CLASS_NOT_EXIST.code
                    return returnValue
                }
            }
        }
        when (existCount) {
            0L -> {
                scheduleTaskEntity.taskName = schedulerDto.taskName
                scheduleTaskEntity.taskType = schedulerDto.taskType
                scheduleTaskEntity.taskDesc = schedulerDto.taskDesc
                scheduleTaskEntity.useYn = schedulerDto.useYn
                scheduleTaskEntity.executeClass = schedulerDto.executeClass
                scheduleTaskEntity.executeQuery = schedulerDto.executeQuery
                scheduleTaskEntity.executeCommand = schedulerDto.executeCommand
                scheduleTaskEntity.executeCycleType = schedulerDto.executeCycleType
                scheduleTaskEntity.executeCyclePeriod = schedulerDto.executeCyclePeriod
                scheduleTaskEntity.cronExpression = schedulerDto.cronExpression
                scheduleTaskEntity.args = schedulerDto.args
                scheduleTaskEntity.src = schedulerDto.src
                scheduleTaskEntity = aliceScheduleTaskRepository.save(scheduleTaskEntity)
                aliceScheduleTaskService.removeTaskFromScheduler(scheduleTaskEntity.taskId)
                if (scheduleTaskEntity.useYn) {
                    aliceScheduleTaskService.addTaskToScheduler(scheduleTaskEntity)
                }
            }
            else -> returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_NAME_DUPLICATION.code
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
        return SchedulerConstants.Status.STATUS_SUCCESS.code
    }

    fun getSchedulerDtoToEntity(schedulerDto: SchedulerDto): AliceScheduleTaskEntity {
        return AliceScheduleTaskEntity(
            taskId = schedulerDto.taskId!!,
            taskType = schedulerDto.taskType,
            taskName = schedulerDto.taskName,
            taskDesc = schedulerDto.taskDesc,
            executeQuery = schedulerDto.executeQuery,
            cronExpression = schedulerDto.cronExpression,
            executeCycleType = schedulerDto.executeCycleType,
            executeCyclePeriod = schedulerDto.executeCyclePeriod,
            executeClass = schedulerDto.executeClass,
            executeCommand = schedulerDto.executeCommand,
            args = schedulerDto.args,
            src = schedulerDto.src,
            useYn = schedulerDto.useYn,
            editable = false
        )
    }

    /**
     * 스케줄 즉시 실행.
     */
    fun immediateExecuteScheduler(schedulerDto: SchedulerDto): String {
        var returnValue = SchedulerConstants.Status.STATUS_SUCCESS.code
        val taskInfo = getSchedulerDtoToEntity(schedulerDto)
        when (schedulerDto.taskType) {
            AliceConstants.ScheduleTaskType.QUERY.code -> {
                taskInfo.executeQuery?.let { scheduleTaskTypeQuery.executeQuery(taskInfo, true) }
            }
            AliceConstants.ScheduleTaskType.CLASS.code -> {
                val fileExist = schedulerDto.executeClass?.let { executeClass ->
                    this.validateClassFile(executeClass)
                } ?: false
                if (fileExist) {
                    try {
                        val runnable = scheduleTaskTypeClass.getRunnable(taskInfo, true)
                        if (runnable != null) {
                            scheduler.schedule(runnable, Instant.now())
                        }
                    } catch (e: Exception) {
                        logger.error("Failed to load class. [{}]", schedulerDto.executeClass)
                        e.printStackTrace()
                        returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULER_EXECUTE.code
                    }
                } else {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_CLASS_NOT_EXIST.code
                }
            }
            AliceConstants.ScheduleTaskType.JAR.code -> {
                val fileExist = schedulerDto.executeCommand?.let { executeCommand ->
                    schedulerDto.src?.let { src -> this.validateJarFile(src, executeCommand) }
                } ?: false
                if (fileExist) {
                    try {
                        val runnable = scheduleTaskTypeJar.getRunnable(taskInfo, true)
                        if (runnable != null) {
                            scheduler.schedule(runnable, Instant.now())
                        }
                    } catch (e: Exception) {
                        logger.error("Failed to load jar. [{}]", schedulerDto.executeCommand)
                        e.printStackTrace()
                        returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULER_EXECUTE.code
                    }
                } else {
                    returnValue = SchedulerConstants.Status.STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST.code
                }
            }
        }
        return returnValue
    }

    fun getSchedulerHistory(
        schedulerHistorySearchCondition: SchedulerHistorySearchCondition
    ): List<AliceScheduleHistoryEntity> {
        return aliceScheduleHistoryRepository.findScheduleHistoryByTaskId(schedulerHistorySearchCondition)
    }

    fun getSchedulerHistoryTotalCount(
        schedulerHistorySearchCondition: SchedulerHistorySearchCondition
    ): Long {
        return aliceScheduleHistoryRepository.countScheduleHistoryByTaskId(schedulerHistorySearchCondition.taskId)
    }

    private fun validateJarFile(src: String, executeCommand: String): Boolean {
        val jarName = executeCommand.replace(" ", "")
        var jarPath = src
        if (jarPath.startsWith("/", true)) {
            jarPath = jarPath.substring(1)
        }
        val jarDir = pluginsDir + File.separator + jarPath
        val files = Paths.get(jarDir).toFile()
        if (jarName.substring(jarName.length - 3, jarName.length) == SchedulerConstants.Types.JAR.type) {
            files.walk().forEach { file ->
                if (file.extension == SchedulerConstants.Extension.JAR.extension) {
                    if (jarName.contains(file.name)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun validateClassFile(executeClass: String): Boolean {
        val classSrc = executeClass.replace(SchedulerConstants.Directory.ADDRESS.code, "")
        val className = classSrc.plus("." + SchedulerConstants.Extension.CLASS.extension)
        val classDir = pluginsDir + File.separator + classSrc + File.separator + className
        return File(classDir).exists()
    }
}
