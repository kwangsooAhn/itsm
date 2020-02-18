package co.brainz.itsm.auth.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@RequestMapping("/auths")
@Controller
public class AuthController() {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)
    private val authEditPage: String = "auth/authEdit"

    /**
     * 역할  설정 뷰를 호출한다.
     */
    @GetMapping("/edit", "")
    public fun getRolelist(request: HttpServletRequest, model: Model): String {

        return authEditPage
    }
}
