package co.brainz.sample.menu

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/menu")
class MenuControllerSample {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mainPage: String = "sample/main"

    @GetMapping("/main")
    fun main(): String {
        return mainPage
    }
}