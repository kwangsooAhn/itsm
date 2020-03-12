package co.brainz.itsm.utility

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Component
class ConvertParam {

    /*
     * String type -> LocalDateTime 타입으로 변환을 위한 함수
     * hg.jung
     *
     */
    fun convertToSearchLocalDateTime(source: String, target: String): LocalDateTime {
        val dateForm = "yyyy-MM-dd"
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateForm)
        val dateFormatterPrefix: DateTimeFormatter
        val dateFormatterSuffix: DateTimeFormatter
        val fromDt: LocalDateTime
        val toDt: LocalDateTime
        if (target == "fromDt") {
            dateFormatterPrefix = DateTimeFormatterBuilder().append(dateFormatter).parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
            fromDt = LocalDateTime.parse(source, dateFormatterPrefix)
            return fromDt
        } else if (target == "toDt") {
            dateFormatterSuffix = DateTimeFormatterBuilder().append(dateFormatter).parseDefaulting(ChronoField.HOUR_OF_DAY, 23).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59).toFormatter()
            toDt = LocalDateTime.parse(source, dateFormatterSuffix)
            return toDt
        } else {
            throw IllegalArgumentException("When you use the parameter type of a method incorrectly")
        }
    }

}
