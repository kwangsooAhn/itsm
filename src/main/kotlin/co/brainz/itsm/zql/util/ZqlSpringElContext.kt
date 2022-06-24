/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ZQL 표현식에서 사용할 수 있는 함수들 모임.
 * 여기에 작성되는 함수들은 ZQL 표현식을 파싱하면서 Context 로 사용된다.
 * 예를 들어 표현식에서 plusYears(closeDt,1) 과 같은 표현식은 closeDt 라는 일시에 1년을 더한 값으로 계산이 된다.
 */
class ZqlSpringElContext {

    /**
     * LocalDateTime 산술 증감 연산 (년)
     */
    fun plusYears(dateTime: String, years: Long): String {
        return this.getLocalDateTime(dateTime).plusYears(years).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (월)
     */
    fun plusMonths(dateTime: String, months: Long): String {
        return this.getLocalDateTime(dateTime).plusMonths(months).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (일)
     */
    fun plusDays(dateTime: String, days: Long): String {
        return this.getLocalDateTime(dateTime).plusDays(days).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (시간)
     */
    fun plusHours(dateTime: String, hours: Long): String {
        return this.getLocalDateTime(dateTime).plusHours(hours).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (분)
     */
    fun plusMinutes(dateTime: String, minutes: Long): String {
        return this.getLocalDateTime(dateTime).plusMinutes(minutes).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 증감 연산 (초)
     */
    fun plusSeconds(dateTime: String, seconds: Long): String {
        return this.getLocalDateTime(dateTime).plusSeconds(seconds).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (년)
     */
    fun minusYears(dateTime: String, years: Long): String {
        return this.getLocalDateTime(dateTime).minusYears(years).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (월)
     */
    fun minusMonths(dateTime: String, months: Long): String {
        return this.getLocalDateTime(dateTime).minusMonths(months).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (일)
     */
    fun minusDays(dateTime: String, days: Long): String {
        return this.getLocalDateTime(dateTime).minusDays(days).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (시간)
     */
    fun minusHours(dateTime: String, hours: Long): String {
        return this.getLocalDateTime(dateTime).minusHours(hours).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (분)
     */
    fun minusMinutes(dateTime: String, minutes: Long): String {
        return this.getLocalDateTime(dateTime).minusMinutes(minutes).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * LocalDateTime 산술 가감 연산 (초)
     */
    fun minusSeconds(dateTime: String, seconds: Long): String {
        return this.getLocalDateTime(dateTime).minusSeconds(seconds).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * 비교 연산자 '=='
     */
    fun equalTo(dateTime: String, otherDateTime: String): Boolean {
        return this.getLocalDateTime(dateTime).isEqual(this.getLocalDateTime(otherDateTime))
    }

    /**
     * 비교 연산자 '!='
     */
    fun notEqualTo(dateTime: String, otherDateTime: String): Boolean {
        return !this.getLocalDateTime(dateTime).isEqual(this.getLocalDateTime(otherDateTime))
    }

    /**
     * 비교 연산자 '>'
     */
    fun greaterThan(dateTime: String, otherDateTime: String): Boolean {
        return this.getLocalDateTime(dateTime).isAfter(this.getLocalDateTime(otherDateTime))
    }

    /**
     * 비교 연산자 '<'
     */
    fun lessThan(dateTime: String, otherDateTime: String): Boolean {
        return this.getLocalDateTime(dateTime).isBefore(this.getLocalDateTime(otherDateTime))
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
     * String 문자열에 대하여 LocalDateTime 타입으로 변경한다.
     * <p>
     * 2가지 포맷의 스트링만 LocalDateTime 타입으로 변환을 지원한다.
     * 하나는 사용자가 입력한 형식인 "yyyy-MM-dd HH:mm:ss" 형식인 경우와
     * 태그의 값으로 날짜시간이 들어간 경우를 위한 ISO 포맷인 "1975-06-23T00:00:00.000Z" 이다.
     *
     * @param dateTime 문자열로 이루어진 날짜시간 데이터
     * @return 변환된 LocalDateTime 타입의 날짜시간 데이터
     */
    private fun getLocalDateTime(dateTime: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        }
    }
}
