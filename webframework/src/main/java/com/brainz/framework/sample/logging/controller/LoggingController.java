package com.brainz.framework.sample.logging.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class LoggingController {
	private static Logger logger = LoggerFactory.getLogger(LoggingController.class);

	@RequestMapping("/sample/logging")
	String LoggingSample() {
		
		String tempString = "logging Sample.";
		
		logger.error("ERROR {}", tempString);
		logger.warn("WARN {}", tempString);
		logger.info("INFO {}", tempString);
		logger.debug("DEBUG {}", tempString);
		logger.trace("TRACE {}", tempString);
		
		return "Hello!";
	}
}
