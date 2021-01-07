package co.brainz

import co.brainz.framework.configuration.AliceFullBeanNameGenerator
import java.util.TimeZone
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableScheduling
@SpringBootApplication
@ComponentScan(nameGenerator = AliceFullBeanNameGenerator::class)
open class ItsmApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    runApplication<ItsmApplication>(*args)
}
