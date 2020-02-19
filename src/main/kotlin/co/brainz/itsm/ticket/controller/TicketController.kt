package co.brainz.itsm.ticket.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/tickets")
class TicketController(private val userService: UserService) {

    private val statusPage: String = "redirect:/certification/status"
    private val ticketSearchPage: String = "ticket/ticketSearch"

    @GetMapping("/ticketSearch")
    fun getTicketSearch(request: HttpServletRequest): String {
        //사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userKey = aliceUserDto.userKey
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        if (userDto.status == UserConstants.Status.SIGNUP.code || userDto.status == UserConstants.Status.EDIT.code) {
            return statusPage
        }

        return ticketSearchPage
    }
}
