package co.brainz

import co.brainz.framework.constants.AliceConstants
import java.util.TimeZone
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableScheduling
@SpringBootApplication
open class ItsmApplication

fun main(args: Array<String>) {
    runApplication<ItsmApplication>(*args)
    TimeZone.setDefault(TimeZone.getTimeZone(AliceConstants.DEFAULT_TIMEZONE))
}
