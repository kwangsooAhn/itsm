package co.brainz.itsm.layout.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/layout")
class LayoutController(
    private val userService: UserService,
    private val currentSessionUser: CurrentSessionUser
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val defaultPage: String = "redirect:/dashboard/view"
    private val menuPage: String = "layout/menu"
    private val statusPage: String = "redirect:/certification/status"

    @GetMapping("/", "")
    fun getLayout(model: Model): String {
        // 사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val userKey = currentSessionUser.getUserKey()
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        return if (userDto.status == AliceUserConstants.Status.SIGNUP.code ||
            userDto.status == AliceUserConstants.Status.EDIT.code
        ) {
            statusPage
        } else {
            defaultPage
        }
    }

    @GetMapping("/menu")
    fun getMenu(): String {
        return menuPage
    }
}
