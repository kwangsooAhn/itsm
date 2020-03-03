package co.brainz.itsm.ticket.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.provider.ProviderUtilities
import co.brainz.itsm.provider.ProviderWorkflow
import co.brainz.itsm.provider.TokenProvider
import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.TokenSaveDto
import co.brainz.itsm.provider.dto.TicketViewDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TicketService(private val tokenProvider: TokenProvider, private val providerWorkflow: ProviderWorkflow) {

    /**
     * Ticket 신규 등록 / 처리
     * isComplete : false일 경우에는 저장, true일 경우에 처리
     *
     * @return Boolean
     */
    fun createTicket(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.postToken(tokenSaveDto)
    }

    /**
     * Ticket 수정 / 처리
     * isComplete : false일 경우에는 수정, true일 경우에 처리
     *
     * @return Boolean
     */
    fun updateTicket(tokenSaveDto: TokenSaveDto): Boolean {
        return tokenProvider.putToken(tokenSaveDto)
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @return List<TicketDto>
     */
    fun getTicketList(): List<TicketViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("status", ProviderConstants.TokenStatus.RUNNING.value)

        val responseBody = providerWorkflow.getProcessInstances(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tickets: List<TicketViewDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, TicketViewDto::class.java))
        for (ticket in tickets) {
            ticket.createDt = ticket.createDt.let { ProviderUtilities().toTimezone(it) }
        }
        return tickets
    }
}