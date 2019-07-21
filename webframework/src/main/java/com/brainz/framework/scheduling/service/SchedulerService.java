package com.brainz.framework.scheduling.service;

public interface SchedulerService {
    
    /**
     * start scheduler.
     */
    void startSampleScheduler(String jobName, String jobGroup);
    
    /**
     * stop scheduler.
     */
    void pauseSampleScheduler(String JobName, String jobGroup);
}
