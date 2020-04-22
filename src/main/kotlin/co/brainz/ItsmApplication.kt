package co.brainz

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
}
