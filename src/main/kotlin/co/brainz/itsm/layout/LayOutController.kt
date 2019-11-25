package co.brainz.itsm.layout

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/layout")
class LayOutController {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/menu")
    fun getLayout(): String {
        return "layout"
    }
}