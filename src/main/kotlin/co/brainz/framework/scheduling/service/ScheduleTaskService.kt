package co.brainz.framework.scheduling.service

import org.springframework.stereotype.Service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.jdbc.core.JdbcTemplate
import javax.annotation.PostConstruct
import co.brainz.framework.scheduling.entity.ScheduleTaskEntity
import java.util.concurrent.ScheduledFuture
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository
import java.util.TimeZone
import org.springframework.scheduling.support.CronTrigger

@Service
public class ScheduleTaskService {
    companion object {
        private val logger = LoggerFactory.getLogger(ScheduleTaskService::class.java)
    }

    @Autowired
    lateinit var scheduler: TaskScheduler

    val taskMap: HashMap<Long, ScheduledFuture<*>?> = hashMapOf<Long, ScheduledFuture<*>?>()

    @Autowired
    lateinit var scheduleTaskRepository: ScheduleTaskRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @PostConstruct
    public fun init() {
        jdbcTemplate.isResultsMapCaseInsensitive = true
    }


    /**
     * task 추가.
     *
     * @param id TASK ID
     * @param task TASK
     * @param taskInfo TASK 정보
     */
    public fun addTaskToScheduler(id: Long, task: Runnable, taskInfo: ScheduleTaskEntity) {
        var scheduledTask: ScheduledFuture<*>? = null
        when (taskInfo.executeCycleType) {
            "fixedDelay" -> scheduledTask = scheduler.scheduleWithFixedDelay(task, taskInfo.executeCyclePeriod)
            "fixedRate" -> scheduledTask = scheduler.scheduleAtFixedRate(task, taskInfo.executeCyclePeriod)
            "cron" -> scheduledTask = scheduler.schedule(
                task,
                CronTrigger(taskInfo.cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().id))
            )
        }
        if (scheduledTask != null) {
            logger.info("The schedule task has been add. {}", taskInfo.toString())
            taskMap[id] = scheduledTask
        }
    }

    /**
     * task 추가.
     * 스케줄링 Task 정보로 Runnable 클래스를 생성하여 Task 추가 메소드를 호출한다.
     *
     * @param taskInfo TASK 정보
     */
    public fun addTaskToScheduler(taskInfo: ScheduleTaskEntity) {
        if ("query" == taskInfo.taskType) {
            addTaskToScheduler(taskInfo.taskId, Runnable { executeQuery(taskInfo.executeQuery) }, taskInfo)
        } else if ("class" == taskInfo.taskType) {
            try {
                val taskClass = Class.forName(taskInfo.executeClass)
                    .asSubclass<Runnable>(Runnable::class.java)
                addTaskToScheduler(taskInfo.taskId, taskClass.getDeclaredConstructor().newInstance(), taskInfo)
            } catch (e: Exception) {
                logger.error("Failed to load class. [{}]", taskInfo.executeClass)
                e.printStackTrace()
            }
        }
    }

    /**
     * task 삭제.
     *
     * @param id TASK ID
     */
    public fun removeTaskFromScheduler(id: Long) {
        val scheduledTask: ScheduledFuture<*>? = taskMap[id]
        if (scheduledTask != null) {
            scheduledTask.cancel(true)
            taskMap[id] = null
            logger.info("The schedule task has been removed. (task_id: {})", id)
        }
    }

    @EventListener(ContextRefreshedEvent::class)
    public fun contextRefreshedEvent() {
        val scheduleTask: MutableList<ScheduleTaskEntity> = scheduleTaskRepository.findAll()
        scheduleTask.forEach { list -> addTaskToScheduler(list) }
    }

    /**
     * 쿼리를 실행한다.
     *
     * @param executeQuery 실행쿼리
     */
    public fun executeQuery(executeQuery: String) {
        jdbcTemplate.execute(executeQuery)
        logger.info("The query has been executed. [{}]", executeQuery)
    }
}