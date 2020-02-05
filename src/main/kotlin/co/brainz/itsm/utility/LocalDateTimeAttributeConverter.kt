package co.brainz.itsm.utility

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.AttributeConverter

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
            val timezone = ConvertParam().timezone()
            return if (timezone.isNotEmpty()) {
                sqlTimestamp.toLocalDateTime()?.atZone(ZoneId.of("UTC"))?.withZoneSameInstant(ZoneId.of(timezone))?.toLocalDateTime()
            } else {
                sqlTimestamp.toLocalDateTime()
            }
        }
    }

}
