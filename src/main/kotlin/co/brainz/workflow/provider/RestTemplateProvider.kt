package co.brainz.workflow.provider

import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import java.net.URI
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

/**
 * 워크플로우 호출을 위해 필요한 메서드를 제공하는 클래스
 *
 * @history
 * 2020-03-13 kbh initialize
 * 2020-03-13 kbh makeTokenData 메서드는 일단 넣을 곳이 마땅치 않아서 여기에 넣어둔다.
 *                다른 workflow 관련 메서드들이 많아지면 확장해야할 것 같다.
 */
@Service
class RestTemplateProvider(private val restTemplate: RestTemplate) {

    @Value("\${server.protocol}")
    private lateinit var protocol: String

    @Value("\${server.port}")
    private lateinit var port: String

    private val keyRegex = "\\{([a-zA-Z]*)}".toRegex()

    fun getKeyRegex(): Regex {
        return this.keyRegex
    }

    /**
     * Set URI.
     *
     * @param restTemplateUrlDto
     * @return uri
     */
    private fun makeUri(restTemplateUrlDto: RestTemplateUrlDto): URI {
        if (restTemplateUrlDto.protocol.isNotEmpty()) {
            this.protocol = restTemplateUrlDto.protocol
        }
        if (restTemplateUrlDto.port.isNotEmpty()) {
            this.port = restTemplateUrlDto.port
        }
        val httpUrl = protocol + "://" + restTemplateUrlDto.serverUrl + ":" + port + restTemplateUrlDto.callUrl
        val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl)
        if (restTemplateUrlDto.parameters.isNotEmpty()) {
            uriComponentsBuilder.queryParams(restTemplateUrlDto.parameters)
        }
        return uriComponentsBuilder.build().toUri()
    }

    /**
     * Set HttpEntity.
     *
     * @param dto
     * @return HttpEntity
     */
    private fun setHttpEntity(dto: Any): HttpEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return HttpEntity(dto, headers)
    }

    fun get(restTemplateUrlDto: RestTemplateUrlDto): String {
        val url = this.makeUri(restTemplateUrlDto)
        val responseString: String?
        try {
            responseString = restTemplate.getForObject(url, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseString ?: ""
    }

    fun create(restTemplateUrlDto: RestTemplateUrlDto, dto: Any): ResponseEntity<String> {
        val url = this.makeUri(restTemplateUrlDto)
        val responseEntity: ResponseEntity<String>
        try {
            responseEntity = restTemplate.postForEntity(url, dto, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseEntity
    }

    fun createToSave(restTemplateUrlDto: RestTemplateUrlDto, dto: Any): ResponseEntity<String> {
        val url = this.makeUri(restTemplateUrlDto)
        val requestEntity = this.setHttpEntity(dto)
        val responseEntity: ResponseEntity<String>
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseEntity
    }

    fun update(restTemplateUrlDto: RestTemplateUrlDto, dto: Any): ResponseEntity<String> {
        val url = this.makeUri(restTemplateUrlDto)
        val requestEntity = this.setHttpEntity(dto)
        val responseEntity: ResponseEntity<String>
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseEntity
    }

    fun delete(restTemplateUrlDto: RestTemplateUrlDto): ResponseEntity<String> {
        val url = this.makeUri(restTemplateUrlDto)
        val requestEntity = HttpEntity(null, null)
        val responseEntity: ResponseEntity<String>
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseEntity
    }

    fun delete(restTemplateUrlDto: RestTemplateUrlDto, dto: Any): ResponseEntity<String> {
        val url = this.makeUri(restTemplateUrlDto)
        val requestEntity = this.setHttpEntity(dto)
        val responseEntity: ResponseEntity<String>
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String::class.java)
        } catch (e: Exception) {
            throw e
        }
        return responseEntity
    }
}
