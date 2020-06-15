package co.brainz.framework.util

import co.brainz.framework.auth.dto.AliceUserDto
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.context.SecurityContextHolder

class AliceTimezoneUtils {

    fun timezone(): String {
        val isAnonymous =
            AuthenticationTrustResolverImpl().isAnonymous(SecurityContextHolder.getContext().authentication)
        var timezone = ""
        when (isAnonymous) {
            false -> {
                if (SecurityContextHolder.getContext().authentication != null) {
                    val aliceUserDto: AliceUserDto =
                        SecurityContextHolder.getContext().authentication.details as AliceUserDto
                    timezone = aliceUserDto.timezone
                }
            }
        }
        return timezone
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
        val timezone = this.timezone()
        if (timezone.isNotEmpty()) {
            localDateTime = LocalDateTime.parse(value, dateTimeFormatter).atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
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
        val timezone = this.timezone()
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
        val timezone = this.timezone()
        if (timezone.isNotEmpty()) {
            return localDateTime.atZone(ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        }
        return localDateTime
    }

    /**
     * LocalDateTime -> ZonedDateTime.
     *
     * @param localDateTime
     * @return ZoneDateTime
     */
    fun toZonedDateTime(localDateTime: LocalDateTime): ZonedDateTime {
        val timezone = this.timezone()
        if (timezone.isNotEmpty()) {
            return ZonedDateTime.of(localDateTime, ZoneId.of(timezone))
        }
        return ZonedDateTime.of(localDateTime, ZoneId.of("UTC"))
    }
}
