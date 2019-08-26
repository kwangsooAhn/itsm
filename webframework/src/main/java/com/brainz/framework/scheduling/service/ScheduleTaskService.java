package com.brainz.framework.scheduling.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.brainz.framework.scheduling.model.ScheduleTask;
import com.brainz.framework.scheduling.repository.ScheduleTaskRepository;

@Service
public class ScheduleTaskService {
    private static Logger logger = LoggerFactory.getLogger(ScheduleTaskService.class);

    @Autowired
    TaskScheduler scheduler;

    Map<Long, ScheduledFuture<?>> taskMap = new HashMap<>();

    @Autowired
    ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }
    
    /**
     * task 추가.
     * 
     * @param id       TASK ID
     * @param task     TASK
     * @param taskInfo TASK 정보
     */
    public void addTaskToScheduler(long id, Runnable task, ScheduleTask taskInfo) {
        ScheduledFuture<?> scheduledTask = null;
        switch (taskInfo.getExecuteCycleType()) {
        case "fixedDelay":
            scheduledTask = scheduler.scheduleWithFixedDelay(task, taskInfo.getExecuteCyclePeriod());
            break;
        case "fixedRate":
            scheduledTask = scheduler.scheduleAtFixedRate(task, taskInfo.getExecuteCyclePeriod());
            break;
        case "cron":
            scheduledTask = scheduler.schedule(task,
                    new CronTrigger(taskInfo.getCronExpression(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));
            break;
        default:
            break;
        }
        taskMap.put(id, scheduledTask);
    }
    
    /**
     * task 추가.
     * 스케줄링 Task 정보로 Runnable 클래스를 생성하여 Task 추가 메소드를 호출한다.
     * 
     * @param taskInfo TASK 정보
     */
    public void addTaskToScheduler(ScheduleTask taskInfo) {
        if ("query".equals(taskInfo.getTaskType())) {
            addTaskToScheduler(taskInfo.getTaskId(), new Runnable() {
                @Override
                public void run() {
                    executeQuery(taskInfo.getExecuteQuery());
                }
            }, taskInfo);
        } else if ("class".equals(taskInfo.getTaskType())) {
            try {
                Class<? extends Runnable> taskClass = Class.forName(taskInfo.getExecuteClass()).asSubclass(Runnable.class);
                addTaskToScheduler(taskInfo.getTaskId(), taskClass.getDeclaredConstructor().newInstance(), taskInfo);
            } catch (Exception e) {
                logger.error("FAILED TO LOAD CLASS [{}]", taskInfo.getExecuteClass());
                e.printStackTrace();
            }
        }
	}

    /**
     * task 삭제.
     * 
     * @param id TASK ID
     */
    public void removeTaskFromScheduler(long id) {
        ScheduledFuture<?> scheduledTask = taskMap.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            taskMap.put(id, null);
        }
    }

    @EventListener({ ContextRefreshedEvent.class })
    void contextRefreshedEvent() {
        // 테스용
        /*************************************/
        /*
        ScheduleTask task = new ScheduleTask();
        task.setTaskType("query");
        task.setExecuteQuery("call JJE_TEST()");
        task.setExecuteCycleType("cron");
        task.setCronExpression("0 * * * * *");
        scheduleTaskRepository.save(task);
        task = new ScheduleTask();
        task.setTaskType("class");
        task.setexecuteClass("com.brainz.framework.scheduling.task.SampleTask");
        task.setExecuteCycleType("cron");
        task.setCronExpression("2 * * * * *");
        scheduleTaskRepository.save(task);
        */
        /*************************************/
        List<ScheduleTask> scheduleTask = scheduleTaskRepository.findAll();
        scheduleTask.forEach(list -> addTaskToScheduler(list));
    }
    
    /**
     * 쿼리를 실행한다.
     * 
     * @param executeQuery 실행쿼리
     */
    public void executeQuery(String executeQuery) {
        jdbcTemplate.execute(executeQuery);
        logger.info("QUERY [{}] IS EXECUTED", executeQuery);
    }
}
