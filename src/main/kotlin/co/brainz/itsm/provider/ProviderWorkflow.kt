package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class ProviderWorkflow(private val restTemplate: RestTemplate): ProviderUtilities() {

    /**
     * Process Instance 리스트 조회.
     *
     * @return String
     */
    fun getProcessInstances(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Workflow.GET_INSTANCES.url, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }
}
