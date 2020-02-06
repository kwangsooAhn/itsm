package co.brainz.itsm.provider

import co.brainz.itsm.provider.dto.UrlDto
import co.brainz.itsm.utility.ConvertParam
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

open class ProviderUtilities {

    fun makeUri(urlDto: UrlDto): URI {
        val httpUrl = urlDto.protocol + "://" + urlDto.serverUrl + ":" + urlDto.port + urlDto.callUrl
        val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl)
        if (urlDto.parameters.isNotEmpty()) {
            uriComponentsBuilder.queryParams(urlDto.parameters)
        }
        return uriComponentsBuilder.build().toUri()
    }

    /**
     * GMT -> Timezone
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
     * GMT -> Timezone
     */
    fun toTimezone(localDateTime: LocalDateTime): LocalDateTime {
        val timezone = ConvertParam().timezone()
        if (timezone.isNotEmpty()) {
            return localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
        }
        return localDateTime
    }

    /**
     * Timezone -> GMT
     */
    fun toGMT(localDateTime: LocalDateTime): LocalDateTime {
        val timezone = ConvertParam().timezone()
        if (timezone.isNotEmpty()) {
            return localDateTime.atZone(ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        }
        return localDateTime
    }

}
