package co.brainz.itsm.layout.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import co.brainz.itsm.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.Model

@Controller
@RequestMapping("/layout")
class LayoutController(private val noticeService: NoticeService, private val userService: UserService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val layoutPage: String = "layout/layout"
    private val menuPage: String = "layout/menu"
    private val statusPage: String = "redirect:/certification/status"

    @GetMapping("/", "")
    fun getLayout(model: Model): String {
        model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: AliceUserEntity = userService.selectUser(userId)
        if (userDto.status == UserConstants.Status.SIGNUP.code) {
            return statusPage
        }

        return layoutPage
    }

    @GetMapping("/menu")
    fun getMenu(): String {
        return menuPage
    }
}
