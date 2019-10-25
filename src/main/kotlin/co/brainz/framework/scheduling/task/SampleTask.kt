package co.brainz.framework.scheduling.task

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component

@Component
public class SampleTask : Runnable {

	companion object {
		private val logger : Logger = LoggerFactory.getLogger(SampleTask::class.java)
	}
	
	override fun run() : Unit {
		logger.info("Sample Task Execute!!")
	}
}
