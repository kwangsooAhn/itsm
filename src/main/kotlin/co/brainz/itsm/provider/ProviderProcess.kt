package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.ProcessDto
import co.brainz.itsm.provider.dto.UrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class ProviderProcess(private val restTemplate: RestTemplate): ProviderUtilities() {

    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    private val regex = "\\{([a-zA-Z]*)}".toRegex()

    fun wfGetProcessList(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESS_LIST.url, port = port, protocol = ProviderConstants.Protocol.HTTPS.value, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfGetProcess(processId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.GET_PROCESS.url.replace(regex, processId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfPostProcess(processDto: ProcessDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.POST_PROCESS.url, port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val responseJson = restTemplate.postForEntity(url, processDto, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    fun wfPutProcess(processDto: ProcessDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.PUT_PROCESS.url.replace(regex, processDto.processId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val objectMapper = ObjectMapper()
        val parameters: LinkedMultiValueMap<*, *>? = objectMapper.convertValue(processDto, LinkedMultiValueMap::class.java)
        val requestEntity = HttpEntity(parameters, headers)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

    fun wfDeleteProcess(processId: String): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Process.DELETE_PROCESS.url.replace(regex, processId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val requestEntity = HttpEntity(null, null)
        val responseJson = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }

}
