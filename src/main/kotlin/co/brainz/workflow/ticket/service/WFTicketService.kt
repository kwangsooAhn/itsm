package co.brainz.workflow.ticket.service

import co.brainz.workflow.ticket.dto.TicketDto
import org.springframework.stereotype.Service

@Service
class WFTicketService {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<TicketDto>
     */
    fun ticketList(): List<TicketDto> {
        //TODO 프로세스에 매핑된 문서 중 상태가 발행인 문서를 조회한다.
        val ticketList = mutableListOf<TicketDto>()
        val ticket1 = TicketDto(
                ticketId = "fbf0e2c7175245df9260c7f94ff42bf6",
                ticketName = "인프라 변경",
                ticketDesc = "인프라 변경 관련 사항을 접수하는 문서양식"
        )
        val ticket2 = TicketDto(
                ticketId = "16cf1961700e4edbbf5251d7b84b3b99",
                ticketName = "단순문의",
                ticketDesc = "단순한 문의사항 접수하는 문서양식"
        )
        ticketList.add(ticket1)
        ticketList.add(ticket2)

        return ticketList
    }

    /**
     * 신청서 1건 조회.
     *
     * @param ticketId
     * @return TicketDto
     */
    fun ticket(ticketId: String): TicketDto {
        //TODO 프로세스에 매핑된 문서 중 상태가 발행인 문서를 조회한다. 1건.
        return TicketDto(
                ticketId = "fbf0e2c7175245df9260c7f94ff42bf6",
                ticketName = "인프라 변경",
                ticketDesc = "인프라 변경 관련 사항을 접수하는 문서양식"
        )
    }
}