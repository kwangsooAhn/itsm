/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.calendar.service.CalendarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/calendars")
class CalendarRestController(
    private val calendarService: CalendarService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 캘린더별 전체 데이터 조회
     */
    @GetMapping("/", "")
    fun getCalendars(): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.getCalendars())
    }

    /**
     * 스케줄 상세 조회
     */
    @GetMapping("/{calendarId}/schedule/{scheduleId}")
    fun getCalendarSchedule(
        @PathVariable calendarId: String,
        @PathVariable scheduleId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.getCalendarSchedule(calendarId, scheduleId))
    }

    @PostMapping("/{calendarId}/schedule")
    fun postCalendarSchedule(
        @PathVariable calendarId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.postCalendarSchedule(calendarId))
    }

    @PutMapping("/{calendarId}/schedule/{scheduleId}")
    fun putCalendarSchedule(
        @PathVariable calendarId: String,
        @PathVariable scheduleId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.putCalendarSchedule(calendarId, scheduleId))
    }

    @DeleteMapping("/{calendarId}/schdule/{scheduleId}")
    fun deleteCalendarSchedule(
        @PathVariable calendarId: String,
        @PathVariable scheduleId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.deleteCalendarSchedule(calendarId, scheduleId))
    }

}
