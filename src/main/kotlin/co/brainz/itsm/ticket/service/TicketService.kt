package co.brainz.itsm.ticket.service

import co.brainz.itsm.provider.ProviderWorkflow
import co.brainz.itsm.provider.dto.TicketDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class TicketService(private val providerWorkflow: ProviderWorkflow) {

    /**
     * 신청서 리스트 조회.
     *
     * @return List<TicketDto>
     */
    fun findTicketList(): List<TicketDto> {
        val responseBody = providerWorkflow.getTickets()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        return mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java,
                TicketDto::class.java))
    }
}