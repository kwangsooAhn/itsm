package co.brainz.itsm.layout.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.notice.service.NoticeService
import co.brainz.itsm.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

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
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userKey = aliceUserDto.userKey
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        if (userDto.status == AliceUserConstants.Status.SIGNUP.code || userDto.status == AliceUserConstants.Status.EDIT.code) {
            return statusPage
        }

        return layoutPage
    }

    @GetMapping("/menu")
    fun getMenu(): String {
        return menuPage
    }
}
