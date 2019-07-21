package com.brainz.framework.job.service.impl;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.brainz.framework.job.component.JobScheduleCreator;
import com.brainz.framework.job.sample.SampleJob;
import com.brainz.framework.job.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobScheduleCreator scheduleCreator;

    @Override
    public void startSampleScheduler() {
        String jobName = "QuartzJob";
        String jobGroup = "brainz";
        Long repeatTime = 1000L;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(SampleJob.class).withIdentity(jobName, jobGroup).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {
                jobDetail = scheduleCreator.createJob(SampleJob.class, false, context, jobName, jobGroup);
                Trigger trigger = scheduleCreator.createSimpleTrigger(jobName, new Date(),
                        repeatTime, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
