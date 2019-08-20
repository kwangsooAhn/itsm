package com.brainz.framework.scheduling.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainz.framework.scheduling.service.SchedulerService;

@RestController
@SpringBootApplication
public class SchedulingController {
    private static Logger logger = LoggerFactory.getLogger(SchedulingController.class);
    
    @Autowired
    SchedulerService schedulerService;

    @RequestMapping("/sample/scheduling/start")
    String startScheduling() {
        logger.debug("sample job started!!!");
        schedulerService.startSampleScheduler("sample1", "brainz");
        return "Start!!";
    }
    
    @RequestMapping("/sample/scheduling/pause")
    String pauseScheduling() {
        logger.debug("sample job paused!!!");
        schedulerService.pauseSampleScheduler("sample1", "brainz");
        return "Pause!!";
    }
}
