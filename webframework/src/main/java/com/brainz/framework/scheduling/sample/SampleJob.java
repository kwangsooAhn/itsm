package com.brainz.framework.scheduling.sample;

import java.util.stream.IntStream;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.brainz.framework.sample.logging.WebframeworkLogging;

@DisallowConcurrentExecution
public class SampleJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(WebframeworkLogging.class);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Sample Job Start");
        IntStream.range(0, 10).forEach(i -> {
            logger.info("Counting - {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        });
        logger.info("Sample Job End");
    }
}
