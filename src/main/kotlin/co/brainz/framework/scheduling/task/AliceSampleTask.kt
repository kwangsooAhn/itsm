package co.brainz.framework.scheduling.task

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component

@Component
public class AliceSampleTask : Runnable {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AliceSampleTask::class.java)
    }

    override fun run() {
        logger.info("Sample Task Execute!!")
    }
}
