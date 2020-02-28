package co.brainz.itsm.ticket.service

import co.brainz.itsm.provider.TokenProvider
import co.brainz.itsm.provider.dto.TokenSaveDto
import org.springframework.stereotype.Service

@Service
class TicketService(private val tokenProvider: TokenProvider) {

    /**
     * Ticket 신규 등록 / 처리
     * isComplete : false일경우에는 저장, true일경우에 처리
     *
     * @return : Boolean
     */
    fun createTicket(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.postToken(tokenSaveDto)
    }

    /**
     * Ticket 수정 / 처리
     * isComplete : false일경우에는 수정, true일경우에 처리
     *
     * @return Boolean
     */
    fun updateTicket(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.putToken(tokenSaveDto)
    }
}
