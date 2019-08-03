package com.brainz.framework.scheduling.service.impl;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.brainz.framework.sample.logging.controller.LoggingController;
import com.brainz.framework.scheduling.component.JobScheduleCreator;
import com.brainz.framework.scheduling.sample.SampleJob;
import com.brainz.framework.scheduling.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private static Logger logger = LoggerFactory.getLogger(LoggingController.class);
    
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobScheduleCreator scheduleCreator;

    @Override
    public void startSampleScheduler(String jobName, String jobGroup) {
        Long repeatTime = 1000L;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(SampleJob.class).withIdentity(jobName, jobGroup).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {
            	logger.info("create job!!!!!!!!!!!!!!!!");
                jobDetail = scheduleCreator.createJob(SampleJob.class, false, context, jobName, jobGroup);
                Trigger trigger = scheduleCreator.createSimpleTrigger(jobName, new Date(),
                        repeatTime, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

                scheduler.scheduleJob(jobDetail, trigger);
            } else {
            	logger.info("start job!!!!!!!!!!!!!!!!");
                schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobName, jobGroup));
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void pauseSampleScheduler(String jobName, String jobGroup) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
