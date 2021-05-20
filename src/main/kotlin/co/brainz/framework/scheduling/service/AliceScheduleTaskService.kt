/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeClass
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeJar
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeQuery
import java.time.LocalDateTime
import java.util.TimeZone
import java.util.concurrent.ScheduledFuture
import javax.annotation.PostConstruct
import kotlin.collections.HashMap
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service

@Service
class AliceScheduleTaskService(
    private val scheduler: TaskScheduler,
    private val aliceScheduleTaskRepository: AliceScheduleTaskRepository,
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val scheduleTaskTypeQuery: ScheduleTaskTypeQuery,
    private val scheduleTaskTypeClass: ScheduleTaskTypeClass,
    private val scheduleTaskTypeJar: ScheduleTaskTypeJar
) {
    private val logger = LoggerFactory.getLogger(AliceScheduleTaskService::class.java)

    val taskMap: HashMap<String, ScheduledFuture<*>?> = hashMapOf()

    @PostConstruct
    fun init() {
        jdbcTemplate.isResultsMapCaseInsensitive = true
    }

    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {
        val scheduleTask: MutableList<AliceScheduleTaskEntity> = aliceScheduleTaskRepository.findByScheduleListByUse()
        scheduleTask.forEach { list -> addTaskToScheduler(list) }
    }

    /**
     * task 추가.
     * 스케줄링 Task 정보로 Runnable 클래스를 생성하여 Task 추가 메소드를 호출한다.
     *
     * @param taskInfo TASK 정보
     */
    fun addTaskToScheduler(taskInfo: AliceScheduleTaskEntity) {
        val task = when (taskInfo.taskType) {
            AliceConstants.ScheduleTaskType.QUERY.code -> scheduleTaskTypeQuery.getRunnable(taskInfo)
            AliceConstants.ScheduleTaskType.CLASS.code -> scheduleTaskTypeClass.getRunnable(taskInfo)
            AliceConstants.ScheduleTaskType.JAR.code -> scheduleTaskTypeJar.getRunnable(taskInfo)
            else -> null
        }
        if (task != null) {
            this.executeScheduler(taskInfo.taskId, task, taskInfo)
        }
    }

    /**
     * task 삭제.
     *
     * @param id TASK ID
     */
    fun removeTaskFromScheduler(id: String) {
        val scheduledTask: ScheduledFuture<*>? = taskMap[id]
        if (scheduledTask != null) {
            scheduledTask.cancel(true)
            taskMap[id] = null
            logger.info("The schedule task has been removed. (task_id: {})", id)
        }
    }

    /**
     * task 추가(실행).
     *
     * @param id TASK ID
     * @param task TASK
     * @param taskInfo TASK 정보
     */
    private fun executeScheduler(id: String, task: Runnable, taskInfo: AliceScheduleTaskEntity) {
        var scheduledTask: ScheduledFuture<*>? = null
        when (taskInfo.executeCycleType) {
            AliceConstants.ScheduleExecuteCycleType.FIXED_DELAY.code -> {
                scheduledTask = scheduler.scheduleWithFixedDelay(task, taskInfo.executeCyclePeriod!!)
            }
            AliceConstants.ScheduleExecuteCycleType.FIXED_RATE.code -> {
                scheduledTask = scheduler.scheduleAtFixedRate(task, taskInfo.executeCyclePeriod!!)
            }
            AliceConstants.ScheduleExecuteCycleType.CRON.code -> {
                scheduledTask = scheduler.schedule(
                    task,
                    CronTrigger(taskInfo.cronExpression!!, TimeZone.getTimeZone(TimeZone.getDefault().id))
                )
            }
        }
        if (scheduledTask != null) {
            logger.info("The schedule task has been add. {}", taskInfo.toString())
            taskMap[id] = scheduledTask

            if (scheduledTask.isDone) {
                aliceScheduleHistoryRepository.save(
                    AliceScheduleHistoryEntity(
                        historySeq = 0L,
                        taskId = taskInfo.taskId,
                        executeTime = LocalDateTime.now(),
                        result = true
                    )
                )
            }
        }
    }
}
