package co.brainz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController
import org.springframework.scheduling.annotation.EnableScheduling

@RestController
@EnableScheduling
@SpringBootApplication
open class ItsmApplication

fun main(args: Array<String>) {
	runApplication<ItsmApplication>(*args)
}
