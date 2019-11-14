package co.brainz.itsm.utility

import org.springframework.stereotype.Component
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import java.time.LocalDate
import org.springframework.core.convert.converter.Converter
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.format.DateTimeFormatterBuilder
import java.time.LocalDateTime

@Component
public class ConvertParam {

/*
 * String type -> LocalDateTime 타입으로 변환을 위한 함수
 * hg.jung
 *
 */
   public fun convertToLocalDateTime(source: String, target: String): LocalDateTime {

      var dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

      if (target == "fromDate") {
         var dateFormatterPrefix: DateTimeFormatter = DateTimeFormatterBuilder().append(dateFormatter).parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter()
         var fromDate: LocalDateTime = LocalDateTime.parse(source, dateFormatterPrefix)
         return fromDate
      } else if (target == "toDate") {
         var dateFormatterSuffix: DateTimeFormatter = DateTimeFormatterBuilder().append(dateFormatter).parseDefaulting(ChronoField.HOUR_OF_DAY, 23).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59).toFormatter()
         var toDate: LocalDateTime = LocalDateTime.parse(source, dateFormatterSuffix)
         return toDate
      } else {		  
		throw IllegalArgumentException("When you use the parameter type of a method incorrectly")
	  }
   }
}