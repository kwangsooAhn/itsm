package co.brainz.itsm.auth.controller

import co.brainz.itsm.auth.service.AuthService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/auths")
@Controller
class AuthController(private val authService: AuthService) {

    val logger = LoggerFactory.getLogger(AuthController::class.java)
    private val authEditPage: String = "auth/authEdit"

    /**
     * 역할  설정 뷰를 호출한다.
     */
    @GetMapping("/edit", "")
    fun getRolelist(request: HttpServletRequest, model: Model): String {

        val authAllList = authService.getAuthList()
        val menuAllList = authService.getMenuList()
        val urlAllList = authService.getUrlList()
        model.addAttribute("authList", authAllList)
        model.addAttribute("menuList", menuAllList)
        model.addAttribute("urlList", urlAllList)

        return authEditPage
    }
}
