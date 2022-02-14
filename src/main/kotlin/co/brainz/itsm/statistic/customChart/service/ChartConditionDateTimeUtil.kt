/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class ChartConditionDateTimeUtil {

    /**
     * LocalDateTime 산술 증감 연산 (년)
     */
    fun plusYears(dateTime: String, years: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusYears(years).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (월)
     */
    fun plusMonths(dateTime: String, months: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusMonths(months).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (일)
     */
    fun plusDays(dateTime: String, days: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusDays(days).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (시간)
     */
    fun plusHours(dateTime: String, hours: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusHours(hours).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (분)
     */
    fun plusMinutes(dateTime: String, minutes: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusMinutes(minutes).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (초)
     */
    fun plusSeconds(dateTime: String, seconds: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.plusSeconds(seconds).format(DateTimeFormatter.ISO_DATE_TIME)
    }


    /**
     * LocalDateTime 산술 가감 연산 (년)
     */
    fun minusYears(dateTime: String, years: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusYears(years).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (월)
     */
    fun minusMonths(dateTime: String, months: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusMonths(months).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (일)
     */
    fun minusDays(dateTime: String, days: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusDays(days).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (시간)
     */
    fun minusHours(dateTime: String, hours: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusHours(hours).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (분)
     */
    fun minusMinutes(dateTime: String, minutes: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusMinutes(minutes).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (초)
     */
    fun minusSeconds(dateTime: String, seconds: Long): String {
        val target = this.getLocalDateTime(dateTime)
        return target.minusSeconds(seconds).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * 비교 연산자 '=='
     */
    fun equalTo(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '!='
     */
    fun notEqualTo(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return !target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '>'
     */
    fun greaterThan(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return target.isAfter(otherTarget)
    }

    /**
     * 비교 연산자 '<'
     */
    fun lessThan(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return target.isBefore(otherTarget)
    }

    /**
     * 비교 연산자 '>='
     */
    fun greaterThanOrEqualTo(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return target.isAfter(otherTarget) || target.isEqual(otherTarget)
    }

    /**
     * 비교 연산자 '<='
     */
    fun lessThanOrEqualTo(dateTime: String, otherDateTime: String): Boolean {
        val target = this.getLocalDateTime(dateTime)
        val otherTarget = this.getLocalDateTime(otherDateTime)

        return target.isBefore(otherTarget) || target.isEqual(otherTarget)
    }

    /**
     * String 문자열에 대하여 format 검사를 진행하고 LocalDateTime의 타입으로 변경한다.
     */
    fun getLocalDateTime(dateTime: String): LocalDateTime {
        var dateFormatParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        dateFormatParser.isLenient = false
        return try {
            dateFormatParser.parse(dateTime)
            LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            dateFormatParser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormatParser.isLenient = false
            dateFormatParser.parse(dateTime)
            val localDateTime =
                LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val ldtZoned: ZonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"))

            ldtZoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        }
    }
}