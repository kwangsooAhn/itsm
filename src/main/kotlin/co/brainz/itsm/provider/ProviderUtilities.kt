package co.brainz.itsm.provider

import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

open class ProviderUtilities {

    @Value("\${server.protocol}")
    lateinit var protocol: String

    @Value("\${server.port}")
    lateinit var port: String

    protected val keyRegex = "\\{([a-zA-Z]*)}".toRegex()

    /**
     * Set URI.
     *
     * @param urlDto
     * @return uri
     */
    fun makeUri(urlDto: UrlDto): URI {
        if (urlDto.protocol.isNotEmpty()) {
            this.protocol = urlDto.protocol
        }
        if (urlDto.port.isNotEmpty()) {
            this.port = urlDto.port
        }
        val httpUrl = protocol + "://" + urlDto.serverUrl + ":" + port + urlDto.callUrl
        val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl)
        if (urlDto.parameters.isNotEmpty()) {
            uriComponentsBuilder.queryParams(urlDto.parameters)
        }
        return uriComponentsBuilder.build().toUri()
    }

    /**
     * Set HttpEntity.
     *
     * @param dto
     * @return HttpEntity
     */
    fun setHttpEntity(dto: Any): HttpEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return HttpEntity(dto, headers)
    }

}
