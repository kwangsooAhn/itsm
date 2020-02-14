package co.brainz.itsm.ticket.controller

import co.brainz.itsm.ticket.service.TicketService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {

    private val ticketSearchPage: String = "ticket/ticketSearch"
    private val ticketListPage: String = "ticket/ticketList"

    /**
     * 신청서 리스트 호출 화면.
     *
     * @return String
     */
    @GetMapping("/search")
    fun getTicketSearch(): String {
        return ticketSearchPage
    }

    /**
     * 신청서 리스트 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getTicketList(model: Model): String {
        model.addAttribute("ticketList", ticketService.findTicketList())
        return ticketListPage
    }

    /**
     * 신청서 작성 화면.
     *
     * @param ticketId
     */
    @GetMapping("{ticketId}/edit")
    fun getTicketEdit(@PathVariable ticketId: String) {
        //TODO return 신청서 작성 화면.
    }
}