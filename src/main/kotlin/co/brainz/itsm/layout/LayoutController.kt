package co.brainz.itsm.layout

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/layout")
class LayoutController {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val layoutViewPage: String = "layout/layout"
    private val menuTestViewPage: String = "menuTest"
    private val authFailedViewPage: String = "authFailed"

    @GetMapping("/", "")
    fun getLayout(): String {
        return layoutViewPage
    }

    @GetMapping("/menuTest")
    fun getMenu(): String {
        return menuTestViewPage
    }

    @GetMapping("/authFailed")
    fun getAuthFailed(): String {
        return authFailedViewPage
    }
}
