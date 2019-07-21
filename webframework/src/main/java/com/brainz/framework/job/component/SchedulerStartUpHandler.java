package com.brainz.framework.job.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.brainz.framework.job.service.SchedulerService;
import com.brainz.framework.sample.logging.WebframeworkLogging;

@Component
public class SchedulerStartUpHandler implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(WebframeworkLogging.class);

    @Autowired
    private SchedulerService schedulerService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            schedulerService.startSampleScheduler();
        } catch (Exception e) {
            logger.error("Schedule startup - error", e);
        }
    }

}
