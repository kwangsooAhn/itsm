package com.brainz.framework.scheduling.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(SampleTask.class);

    @Override
    public void run() {
        logger.info("Sample Task Execute!!");
    }

}
