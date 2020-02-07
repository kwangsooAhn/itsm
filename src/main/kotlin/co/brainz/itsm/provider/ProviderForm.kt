package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.FormDto
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
class ProviderForm(private val restTemplate: RestTemplate): ProviderUtilities() {

    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    private val regex = "\\{([a-zA-Z]*)}".toRegex()

    fun wfGetFormListToJson(params: LinkedMultiValueMap<String, String>): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.GET_FORM_LIST.url, port = port, protocol = ProviderConstants.Protocol.HTTPS.value, parameters = params))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfGetFormToJson(formId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.GET_FORM.url.replace(regex, formId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    fun wfPostForm(formDto: FormDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.POST_FORM.url, port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val objectMapper = ObjectMapper()
        val parameters: LinkedMultiValueMap<*, *>? = objectMapper.convertValue(formDto, LinkedMultiValueMap::class.java)
        println(">>>>>>parameters")
        println(parameters)
        val requestEntity = HttpEntity(parameters, headers)
        println(restTemplate.postForObject(url, requestEntity, Boolean::class.java))
        return true
        //return restTemplate.postForObject(url, requestEntity, Boolean::class.java)?:false
    }

    fun wfPutForm(formDto: FormDto): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.PUT_FORM.url.replace(regex, formDto.formId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val objectMapper = ObjectMapper()
        val parameters: LinkedMultiValueMap<*, *>? = objectMapper.convertValue(formDto, LinkedMultiValueMap::class.java)
        val requestEntity = HttpEntity(parameters, headers)
        val responseJson = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        //TODO: 파싱
        return false
    }

    fun wfDeleteForm(formId: String): Boolean {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Form.DELETE_FORM.url.replace(regex, formId), port = port, protocol = ProviderConstants.Protocol.HTTPS.value))
        val requestEntity = HttpEntity(null, null)
        val responseJson = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        return responseJson.statusCode == HttpStatus.OK
    }




}
