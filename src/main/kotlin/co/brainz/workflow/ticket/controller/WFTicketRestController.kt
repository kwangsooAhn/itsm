package co.brainz.workflow.ticket.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.ticket.dto.TicketDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/tickets")
class WFTicketRestController(private val wfEngine: WFEngine) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<TicketDto>
     */
    @GetMapping("")
    fun getTickets(): List<TicketDto> {
        return wfEngine.ticket().ticketList()
    }

    /**
     * 신청서 1건 조회.
     *
     * @param ticketId
     * @return TicketDto
     */
    @GetMapping("{ticketId}")
    fun getTicket(@PathVariable ticketId: String): TicketDto {
        return wfEngine.ticket().ticket(ticketId)
    }
}