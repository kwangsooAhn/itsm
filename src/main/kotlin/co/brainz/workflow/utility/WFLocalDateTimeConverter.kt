package co.brainz.workflow.utility

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.AttributeConverter

class WFLocalDateTimeConverter(): AttributeConverter<LocalDateTime, Timestamp> {

    override fun convertToDatabaseColumn(locDateTime: LocalDateTime?): Timestamp? {
        return if (locDateTime == null) {
            null
        } else {
            Timestamp.valueOf(locDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())
        }
    }

    override fun convertToEntityAttribute(sqlTimestamp: Timestamp?): LocalDateTime? {
        return sqlTimestamp?.toLocalDateTime()
    }

}
