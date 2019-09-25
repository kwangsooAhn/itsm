/*
package co.brainz.framework.scheduling.service

import org.springframework.stereotype.Service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.jdbc.core.JdbcTemplate
import javax.annotation.PostConstruct
import co.brainz.framework.scheduling.model.ScheduleTask_Kotlin
import java.util.concurrent.ScheduledFuture
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository_Kotlin
import java.util.TimeZone
import org.springframework.scheduling.support.CronTrigger

@Service
public class ScheduleTaskService_Kotlin {
	companion object {
		private val logger = LoggerFactory.getLogger(ScheduleTaskService_Kotlin::class.java)
	}

	@Autowired
	lateinit var scheduler: TaskScheduler

	//Map<Long, ScheduledFuture<?>> taskMap = new HashMap<>();
	val taskMap: HashMap<Long?, ScheduledFuture<*>?> = hashMapOf<Long?, ScheduledFuture<*>?>()

	@Autowired
	lateinit var scheduleTaskRepository: ScheduleTaskRepository_Kotlin


	@Autowired
	lateinit private var jdbcTemplate: JdbcTemplate

	@PostConstruct
	public fun init(): Unit {
		jdbcTemplate.setResultsMapCaseInsensitive(true);
	}


	/**
	 * task 추가.
	 *
	 * @param id TASK ID
	 * @param task TASK
	 * @param taskInfo TASK 정보
	 */
	public fun addTaskToScheduler(id: Long, task: Runnable, taskInfo: ScheduleTask_Kotlin): Unit {
		var scheduledTask: ScheduledFuture<*>? = null
		when (taskInfo.executeCycleType) {
			"fixedDelay" -> scheduledTask = scheduler.scheduleWithFixedDelay(task, taskInfo.executeCyclePeriod)
			"fixedRate" -> scheduledTask = scheduler.scheduleAtFixedRate(task, taskInfo.executeCyclePeriod)
			"cron" -> scheduledTask = scheduler.schedule(
				task,
				CronTrigger(taskInfo.cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID()))
			)
			else -> {
			}
		}
		taskMap.set(id, scheduledTask)
	}

	/**
	 * task 추가.
	 * 스케줄링 Task 정보로 Runnable 클래스를 생성하여 Task 추가 메소드를 호출한다.
	 *
	 * @param taskInfo TASK 정보
	 */
	fun addTaskToScheduler(taskInfo: ScheduleTask_Kotlin) {
		if ("query" == taskInfo.taskType) {
			addTaskToScheduler(taskInfo.taskId, object : Runnable {
				public override fun run() {
					executeQuery(taskInfo.executeQuery)
				}
			}, taskInfo)
		} else if ("class" == taskInfo.taskType) {
			try {
				val taskClass = Class.forName(taskInfo.executeClass)
					.asSubclass<Runnable>(Runnable::class.java)
				addTaskToScheduler(taskInfo.taskId, taskClass.getDeclaredConstructor().newInstance(), taskInfo)
			} catch (e: Exception) {
				logger.error("FAILED TO LOAD CLASS [{}]", taskInfo.executeClass)
				e.printStackTrace()
			}
		}
	}

	/**
	 * task 삭제.
	 *
	 * @param id TASK ID
	 */
	public fun removeTaskFromScheduler(id: Long): Unit {
		val scheduledTask: ScheduledFuture<*>? = taskMap.get(id)
		if (scheduledTask != null) {
			scheduledTask.cancel(true)
			taskMap.set(id, null)
		}
	}

	@EventListener(ContextRefreshedEvent::class)
	public fun contextRefreshedEvent(): Unit {
		//var task = ScheduleTask_Kotlin(
		//taskId =
		//,taskType = "query"
		//,executeClass = "co.brainz.scheduling.Scheduler"
		//,executeQuery = "call JJE_TEST()"
		//,executeCycleType = "cron"
		//,executeCyclePeriod = 5000
		//,cronExpression =  "0 * * * * *"
		//) 

		//scheduleTaskRepository.save(task);

		val scheduleTask = scheduleTaskRepository.findAll()
		scheduleTask.forEach({ list -> addTaskToScheduler(list) })
	}

	/**
	 * 쿼리를 실행한다.
	 *
	 * @param executeQuery 실행쿼리
	 */
	public fun executeQuery(executeQuery: String): Unit {
		jdbcTemplate.execute(executeQuery);
		logger.info("QUERY [{}] IS EXECUTED", executeQuery);
	}
}

*/
