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

import com.brainz.framework.sample.logging.controller.LoggingController;
import com.brainz.framework.scheduling.model.ScheduleTask;
import com.brainz.framework.scheduling.repository.ScheduleTaskRepository;

@Service
public class ScheduleTaskService {
    private static Logger logger = LoggerFactory.getLogger(LoggingController.class);

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
        switch (taskInfo.getRunCycleType()) {
        case "fixedDelay":
            scheduledTask = scheduler.scheduleWithFixedDelay(task, taskInfo.getMilliseconds());
            break;
        case "fixedRate":
            scheduledTask = scheduler.scheduleAtFixedRate(task, taskInfo.getMilliseconds());
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
         task.setRunCycleType("cron");
         task.setCronExpression("0 * * * * *");
         scheduleTaskRepository.save(task);
         task = new ScheduleTask();
         task.setTaskType("class");
         task.setTaskClass("com.brainz.framework.scheduling.task.SampleTask");
         task.setRunCycleType("cron");
         task.setCronExpression("2 * * * * *");
         scheduleTaskRepository.save(task);
         */
        /*************************************/
        List<ScheduleTask> scheduleTask = scheduleTaskRepository.findAll();
        scheduleTask.forEach(list -> {
            if ("query".equals(list.getTaskType())) {
                addTaskToScheduler(list.getId(), new Runnable() {
                    @Override
                    public void run() {
                        callStoreProcedure(list.getExecuteQuery());
                    }
                }, list);
            } else if ("class".equals(list.getTaskType())) {
                try {
                    Class<? extends Runnable> taskClass = Class.forName(list.getTaskClass()).asSubclass(Runnable.class);
                    addTaskToScheduler(list.getId(), taskClass.getDeclaredConstructor().newInstance(), list);
                } catch (Exception e) {
                    logger.error("FAILED TO LOAD CLASS [{}]", list.getTaskClass());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 프로시저를 실행한다.
     * 
     * @param procedureName 프로시저명
     * @param parameters    프로시저 parameter
     */
    public void callStoreProcedure(String executeQuery) {
        jdbcTemplate.execute(executeQuery);
        logger.info("QUERY [{}] IS EXECUTED", executeQuery);
    }
}
