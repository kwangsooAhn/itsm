/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.calendar.dto.CalendarDeleteRequest
import co.brainz.itsm.calendar.dto.CalendarRequest
import co.brainz.itsm.calendar.dto.ScheduleData
import co.brainz.itsm.calendar.service.CalendarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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
    @PostMapping("/", "")
    fun getCalendars(@RequestBody calendarRequest: CalendarRequest): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.getCalendars(calendarRequest))
    }

    /**
     * 일반 일정 등록
     */
    @PostMapping("/{calendarId}/schedule")
    fun postCalendarSchedule(
        @PathVariable calendarId: String,
        @RequestBody data: ScheduleData
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.postCalendarSchedule(calendarId, data))
    }

    /**
     * 일반 일정 수정
     */
    @PutMapping("/{calendarId}/schedule")
    fun putCalendarSchedule(
        @PathVariable calendarId: String,
        @RequestBody data: ScheduleData
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.putCalendarSchedule(calendarId, data))
    }

    /**
     * 일반 일정 삭제
     */
    @DeleteMapping("/{calendarId}/schedule")
    fun deleteCalendarSchedule(
        @PathVariable calendarId: String,
        @RequestBody calendarDeleteRequest: CalendarDeleteRequest
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.deleteCalendarSchedule(calendarId, calendarDeleteRequest))
    }

    /**
     * 반복 일정 등록
     */
    @PostMapping("/{calendarId}/repeat")
    fun postCalendarRepeat(
        @PathVariable calendarId: String,
        @RequestBody data: ScheduleData
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.postCalendarRepeat(calendarId, data))
    }

    /**
     * 반복 일정 수정
     */
    @PutMapping("/{calendarId}/repeat")
    fun putCalendarRepeat(
        @PathVariable calendarId: String,
        @RequestBody data: ScheduleData
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.putCalendarRepeat(calendarId, data))
    }

    /**
     * 반복 일정 삭제
     */
    @DeleteMapping("/{calendarId}/repeat")
    fun deleteCalendarRepeat(
        @PathVariable calendarId: String,
        @RequestBody calendarDeleteRequest: CalendarDeleteRequest
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(calendarService.deleteCalendarRepeat(calendarId, calendarDeleteRequest))
    }

    /**
     * Excel 다운로드
     */
    @PostMapping("/excel")
    fun getCalendarExcelDownload(@RequestBody calendarRequest: CalendarRequest): ResponseEntity<ByteArray> {
        return calendarService.getCalendarExcelDownload(calendarRequest)
    }
}
