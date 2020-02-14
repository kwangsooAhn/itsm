package co.brainz.itsm.provider

import co.brainz.itsm.provider.dto.UrlDto
import co.brainz.itsm.utility.ConvertParam
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
     * GMT -> Timezone.
     *
     * @param value
     * @param dateTimeFormatter
     * @return localDateTime
     */
    fun toTimezone(value: String, dateTimeFormatter: DateTimeFormatter): LocalDateTime {
        var localDateTime = LocalDateTime.parse(value, dateTimeFormatter)
        val timezone = ConvertParam().timezone()
        if (timezone.isNotEmpty()) {
            localDateTime = LocalDateTime.parse(value, dateTimeFormatter).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
        }
        return localDateTime
    }

    /**
     * GMT -> Timezone.
     *
     * @param localDateTime
     * @return localDateTime
     */
    fun toTimezone(localDateTime: LocalDateTime): LocalDateTime {
        val timezone = ConvertParam().timezone()
        if (timezone.isNotEmpty()) {
            return localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
        }
        return localDateTime
    }

    /**
     * Timezone -> GMT.
     *
     * @param localDateTime
     * @return localDateTime
     */
    fun toGMT(localDateTime: LocalDateTime): LocalDateTime {
        val timezone = ConvertParam().timezone()
        if (timezone.isNotEmpty()) {
            return localDateTime.atZone(ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        }
        return localDateTime
    }

    /**
     * Set HttpEntity.
     *
     * @param dto
     * @return HttpEntity
     */
    fun setHttpEntity(dto: Any): HttpEntity<LinkedMultiValueMap<*, *>?> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val objectMapper = ObjectMapper()
        val parameters: LinkedMultiValueMap<*, *>? = objectMapper.convertValue(dto, LinkedMultiValueMap::class.java)
        return HttpEntity(parameters, headers)
    }

}
