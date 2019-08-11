package com.brainz.framework.scheduling.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.brainz.framework.sample.logging.controller.LoggingController;

@Service
public class ScheduleTaskService {
    private static Logger logger = LoggerFactory.getLogger(LoggingController.class);
    
    @Autowired
    TaskScheduler scheduler;
    
    Map<Integer, ScheduledFuture<?>> taskMap = new HashMap<>();

    public ScheduleTaskService(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addTaskToScheduler(int id, Runnable task, String expression) {
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task,
                new CronTrigger(expression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        taskMap.put(id, scheduledTask);
    }

    public void removeTaskFromScheduler(int id) {
        ScheduledFuture<?> scheduledTask = taskMap.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            taskMap.put(id, null);
        }
    }

    @EventListener({ ContextRefreshedEvent.class })
    void contextRefreshedEvent() {
        // 서버 시작 시 실행 이벤트
        // DB 조회 후 스케줄 추가
        //  - procedure 및 class 구분에 따른 실행 로직 구현 필요
        //  - 화면 UI 추가로  수정 로직이 필요할 시에는 remove 후 add 로직 실행하면 되지 않을까?
        // 추가 샘플
        /*
        addTaskToScheduler(1, new Runnable() {
            @Override
            public void run() {
                logger.info("schedule task add test");
            }
        }, "1 * * * * ?");
        */
    }
}
