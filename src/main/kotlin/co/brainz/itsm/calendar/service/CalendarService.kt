/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.repository.CalendarRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CalendarService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarRepository: CalendarRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 캘린더 목록 조회
     */
    fun getCalendarList(): List<CalendarDto> {
        return calendarRepository.getCalendarList(currentSessionUser.getUserKey())
    }

    /**
     * 캘린더별 전체 데이터 조회
     */
    fun getCalendars() {

    }

    /**
     * 스케줄 상세 조회
     */
    fun getCalendarSchedule(calendarId: String, scheduleId: String): Any {
        return ""
    }

    fun postCalendarSchedule(calendarId: String): Any {
        return ""
    }

    fun putCalendarSchedule(calendarId: String, scheduleId: String): Any {
        return ""
    }

    fun deleteCalendarSchedule(calendarId: String, scheduleId: String): Any {
        return ""
    }

}
