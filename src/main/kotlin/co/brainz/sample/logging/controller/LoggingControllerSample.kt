package co.brainz.sample.logging.controller

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingControllerSample {

	companion object {	
	    private val logger = LoggerFactory.getLogger(this::class.java)
	}

	@RequestMapping("/sample/logging")
	fun LoggingSample(): String {
		val tempString: String = "logging Sample."
		logger.error("ERROR {}", tempString);
		logger.warn("WARN {}", tempString);
		logger.info("INFO {}", tempString);
		logger.debug("DEBUG {}", tempString);
		logger.trace("TRACE {}", tempString);

		return "Hello!";
	}
}

