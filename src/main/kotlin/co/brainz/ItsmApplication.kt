package co.brainz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.RestController
import java.util.TimeZone
import javax.annotation.PostConstruct

@RestController
@EnableScheduling
@SpringBootApplication
open class ItsmApplication {

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}

fun main(args: Array<String>) {
    runApplication<ItsmApplication>(*args)
}
