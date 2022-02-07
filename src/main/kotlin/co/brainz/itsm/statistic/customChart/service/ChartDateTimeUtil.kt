package co.brainz.itsm.statistic.customChart.service

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ZonedDateTimeUtil {

    @Value("\${product.version}")
    lateinit var productVersion: String

    /**
     * 비교 연산자 '=='
     */
    fun equalTo(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '!='
     */
    fun notEqualTo(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '>'
     */
    fun greaterThan(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isAfter(otherTarget)
    }

    /**
     * 비교 연산자 '<'
     */
    fun lessThan(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isBefore(otherTarget)
    }

    /**
     * 비교 연산자 '>='
     */
    fun greaterThanOrEqualTo(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isAfter(otherTarget) || target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '<='
     */
    fun lessThanOrEqualTo(date: String, otherDate: String): Boolean {
        val target = this.getLocalDateTime(date)
        val otherTarget = this.getLocalDateTime(otherDate)

        return target.isBefore(otherTarget) || target.isEqual(otherTarget)
    }

    fun getLocalDateTime(dateTime: String): LocalDateTime {
        return try {
            val dateFormatParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            dateFormatParser.isLenient = false
            dateFormatParser.parse(dateTime)
            LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            val dateFormatParser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormatParser.isLenient = false
            dateFormatParser.parse(dateTime)
            val localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val ldtZoned: ZonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))
            ldtZoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        }
    }
}