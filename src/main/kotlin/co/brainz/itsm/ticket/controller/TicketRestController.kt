package co.brainz.itsm.ticket.controller

import co.brainz.itsm.ticket.service.TicketService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/tickets")
class TicketRestController(private val ticketService: TicketService) {

    /**
     * Ticket 신규 등록 / 처리
     */
    @PostMapping("/data")
    fun createTicket(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): String {
        return ticketService.createTicket(restTemplateTokenDto)
    }

    /**
     * Ticket 수정 / 처리
     */
    @PutMapping("/data")
    fun updateTicket(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        return ticketService.updateTicket(restTemplateTokenDto)
    }
}
