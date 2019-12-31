package co.brainz.itsm.portal

import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/portal")
class PortalController {
    
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val portalMainPage: String = "portal/portalMain"

    /**
     * FAQ 검색 화면 호출 처리
     */
    @GetMapping("/portalMain")
    fun getFaqSearch(request: HttpServletRequest, model: Model): String {
        return portalMainPage
    }
}