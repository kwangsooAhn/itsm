package co.brainz.itsm.auth.controller

import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.auth.service.AuthService
import co.brainz.itsm.code.service.CodeService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/auths")
@Controller
class AuthController(
    private val authService: AuthService,
    private val codeService: CodeService
) {

    val logger = LoggerFactory.getLogger(AuthController::class.java)
    private val authEditPage: String = "auth/authEdit"
    private val authListPage: String = "auth/authList"

    /**
     * 권한 설정 화면 호출처리.
     */
    @GetMapping("/edit")
    fun getRolelist(request: HttpServletRequest, model: Model): String {

        val defaultUserMenuList = codeService.selectCodeByParent(AliceUserConstants.DefaultMenu.USER_DEFAULT_MENU.code)
        val defaultUserUrlList = codeService.selectCodeByParent(AliceUserConstants.DefaultUrl.USER_DEFAULT_URL.code)
        val menuAllList = authService.getMenuList()
        val urlAllList = authService.getUrlList()

        model.addAttribute("defaultUserMenuList", defaultUserMenuList)
        model.addAttribute("defaultUserUrlList", defaultUserUrlList)
        model.addAttribute("menuList", menuAllList)
        model.addAttribute("urlList", urlAllList)

        return authEditPage
    }

    /**
     * 권한 설정 검색 결과 리스트 화면 호출 처리.
     */
    @GetMapping("")
    fun getAuthList(search: String, model: Model): String {
        model.addAttribute("authList", authService.getAuthSearchList(search))

        return authListPage
    }
}
