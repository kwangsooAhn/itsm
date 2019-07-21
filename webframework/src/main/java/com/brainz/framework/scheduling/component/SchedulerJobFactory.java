package com.brainz.framework.scheduling.component;

import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.brainz.framework.sample.logging.WebframeworkLogging;

public class SchedulerJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(WebframeworkLogging.class);

    private AutowireCapableBeanFactory beanFactory;
    
    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }
    
    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        logger.info("create job instance");
        beanFactory.autowireBean(job);
        return job;
    }
}
