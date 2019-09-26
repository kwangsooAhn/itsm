package co.brainz.framework.sample.logging.controller

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController_Kotlin {

	companion object {	
	    private val logger = LoggerFactory.getLogger(LoggingController_Kotlin::class.java)
	}

	@RequestMapping("/sample/logging")
	fun LoggingSample(): String {
		val tempString: String = "logging Sample."
		logger.error("ERROR {} Kotlin", tempString);
		logger.warn("WARN {} Kotlin", tempString);
		logger.info("INFO {} Kotlin", tempString);
		logger.debug("DEBUG {} Kotlin", tempString);
		logger.trace("TRACE {} Kotlin", tempString);

		return "Hello!";
	}
}

