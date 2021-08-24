package co.brainz.itsm.auth.controller

import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.auth.dto.AuthSearchCondition
import co.brainz.itsm.auth.service.AuthService
import co.brainz.itsm.code.service.CodeService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/auths")
@Controller
class AuthController(
    private val authService: AuthService,
    private val codeService: CodeService
) {

    val logger = LoggerFactory.getLogger(AuthController::class.java)
    private val authSearchPage: String = "auth/authSearch"
    private val authEditPage: String = "auth/authEdit"
    private val authListPage: String = "auth/authList"

    /**
     * 권한 검색 화면
     */
    @GetMapping("/search")
    fun getAuthSearch(request: HttpServletRequest, model: Model): String {
        return authSearchPage
    }

    /**
     * 권한 설정 검색 결과 리스트 화면 호출 처리.
     */
    @GetMapping("")
    fun getAuthList(authSearchCondition: AuthSearchCondition, model: Model): String {
        val result = authService.getAuthSearchList(authSearchCondition)
        model.addAttribute("authList", result.data)
        model.addAttribute("paging", result.paging)
        return authListPage
    }

    /**
     * 권한 편집 화면
     */
    @GetMapping("/{authId}/edit")
    fun getRoleList(@PathVariable authId: String, model: Model): String {
        model.addAttribute("auth", authService.getAuthDetail(authId))
        model.addAttribute("defaultUserMenuList", codeService.selectCodeByParent(AliceUserConstants.DefaultMenu.USER_DEFAULT_MENU.code))
        model.addAttribute("defaultUserUrlList", codeService.selectCodeByParent(AliceUserConstants.DefaultUrl.USER_DEFAULT_URL.code))
        model.addAttribute("menuList", authService.getMenuList())
        model.addAttribute("urlList", authService.getUrlList())

        return authEditPage
    }
}
