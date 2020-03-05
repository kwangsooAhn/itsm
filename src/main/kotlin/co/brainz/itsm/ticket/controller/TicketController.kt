package co.brainz.itsm.ticket.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.itsm.ticket.service.TicketService
import co.brainz.itsm.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/tickets")
class TicketController(private val userService: UserService, private val ticketService: TicketService) {

    private val statusPage: String = "redirect:/certification/status"
    private val ticketSearchPage: String = "ticket/ticketSearch"
    private val ticketListPage: String = "ticket/ticketList"

    /**
     * 처리할 문서 리스트 호출 화면.
     * 단, 사용자 상태가 SIGNUP인 경우 인증 화면.
     *
     * @param request
     * @return String
     */
    @GetMapping("/search")
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

    /**
     * 처리할 문서 리스트 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getTicketList(model: Model): String {
        model.addAttribute("ticketList", ticketService.getTicketList())
        return ticketListPage
    }
}
