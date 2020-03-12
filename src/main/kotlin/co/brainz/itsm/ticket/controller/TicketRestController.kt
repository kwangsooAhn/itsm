package co.brainz.itsm.ticket.controller

import co.brainz.itsm.provider.dto.TokenDto
import co.brainz.itsm.ticket.service.TicketService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@RestController
@RequestMapping("/rest/tickets")
class TicketRestController(private val ticketService: TicketService) {

    /**
     * Ticket 신규 등록 / 처리
     */
    @PostMapping("/data")
    fun createTicket(@RequestBody tokenDto: TokenDto): Boolean {
        return ticketService.createTicket(tokenDto)
    }

    /**
     * Ticket 수정 / 처리
     */
    @PutMapping("/data")
    fun updateTicket(@RequestBody tokenDto: TokenDto): Boolean {
        return ticketService.updateTicket(tokenDto)
    }
}
