package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ProviderWorkflow(private val restTemplate: RestTemplate): ProviderUtilities() {

    /**
     * 신청서 리스트 조회.
     *
     * @return String
     */
    fun getTickets(): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Workflow.GET_TICKETS.url))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    /**
     * 신청서 1건 조회.
     *
     * @param ticketId
     * @return String
     */
    fun getTicket(ticketId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Workflow.GET_TICKET.url.replace(keyRegex, ticketId)))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun getProcessInstances() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun postProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun putProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun taskComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun taskCompleteToGateway() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
