package co.brainz.itsm.ticket.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTicketDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TicketService(private val restTemplate: RestTemplateProvider) {

    /**
     * Ticket 신규 등록 / 처리
     * isComplete : false일 경우에는 저장, true일 경우에 처리
     *
     * @return Boolean
     */
    fun createTicket(restTemplateTokenDto: RestTemplateTokenDto): String {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN_DATA.url)
        return restTemplate.create(url, restTemplateTokenDto)
    }

    /**
     * Ticket 수정 / 처리
     * isComplete : false일 경우에는 수정, true일 경우에 처리
     *
     * @return Boolean
     */
    fun updateTicket(restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.PUT_TOKEN_DATA.url.replace(restTemplate.getKeyRegex(), restTemplateTokenDto.tokenId))
        return restTemplate.update(url, restTemplateTokenDto)
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @return List<TicketDto>
     */
    fun getTicketList(): List<RestTemplateTicketDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("status", RestTemplateConstants.TokenStatus.RUNNING.value)

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tickets: List<RestTemplateTicketDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateTicketDto::class.java))
        for (ticket in tickets) {
            ticket.createDt = ticket.createDt.let { AliceTimezoneUtils().toTimezone(it) }
        }
        return tickets
    }

    // 아래는 샘플입니다.
    // get token list
    // url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKENS.url, parameters = params)
    // restTemplate.get(url)

    // get token
    // url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKEN.url.replace(keyRegex, tokenId))
    // restTemplate.get(url)

    // get token data
    // url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(keyRegex, tokenId))
    // restTemplate.get(url)

    // update token
    // url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.PUT_TOKEN.url.replace(keyRegex, restTemplateTokenDto.tokenId))
    // restTemplate.update(url, 업데이트할 dto)

}
