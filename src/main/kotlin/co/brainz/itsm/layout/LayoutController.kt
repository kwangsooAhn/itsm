package co.brainz.itsm.layout

import co.brainz.itsm.certification.constants.CertificationConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import co.brainz.itsm.notice.service.NoticeService
import org.springframework.security.core.context.SecurityContextHolder
import co.brainz.itsm.user.UserEntity
import org.springframework.ui.Model
import co.brainz.itsm.user.UserService

@Controller
@RequestMapping("/layout")
class LayoutController(private val noticeService: NoticeService, private val userService: UserService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val layoutPage: String = "layout/layout"
    private val statusPage: String = "redirect:/certification/status"
    private val menuTestPage: String = "menuTest"

    @GetMapping("/", "")
    fun getLayout(model: Model): String {
        model.addAttribute("noticePopUp", noticeService.findNoticePopUp())

        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = userService.selectUser(userId)
        if (userDto.status == CertificationConstants.UserStatus.SIGNUP.code) {
            return statusPage
        }

        return layoutPage
    }

    @GetMapping("/menuTest")
    fun getMenu(): String {
        return menuTestPage
    }
}
