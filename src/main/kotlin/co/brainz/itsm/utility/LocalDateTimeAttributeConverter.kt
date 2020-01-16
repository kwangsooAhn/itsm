package co.brainz.itsm.utility

import co.brainz.framework.auth.dto.AliceUserDto
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.context.SecurityContextHolder
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalDateTimeAttributeConverter(): AttributeConverter<LocalDateTime, Timestamp> {

    override fun convertToDatabaseColumn(locDateTime: LocalDateTime?): Timestamp? {
        return if (locDateTime == null) {
            null
        } else {
            Timestamp.valueOf(locDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        }
    }

    override fun convertToEntityAttribute(sqlTimestamp: Timestamp?): LocalDateTime? {
        return if (sqlTimestamp == null) {
            null
        } else {
            return sqlTimestamp.toLocalDateTime()?.atZone(ZoneId.of("UTC"))?.withZoneSameInstant(ZoneId.of(timezone()))?.toLocalDateTime()
        }
    }

    fun timezone(): String {
        val isAnonymous = AuthenticationTrustResolverImpl().isAnonymous(SecurityContextHolder.getContext().authentication)
        var timezone = TimeZone.getDefault().id
        when (isAnonymous) {
            false -> {
                if (SecurityContextHolder.getContext().authentication != null) {
                    val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
                    timezone = aliceUserDto.timezone
                }
            }
        }
        return timezone
    }

}
