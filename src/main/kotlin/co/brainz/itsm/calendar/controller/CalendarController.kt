/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.controller

import co.brainz.itsm.calendar.service.CalendarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/calendars")
class CalendarController(
    private val calendarService: CalendarService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val calendarPage: String = "calendar/calendar"

    /**
     * 일정 관리 화면
     */
    @GetMapping("")
    fun getCalendar(model: Model): String {
        model.addAttribute("calendarList", calendarService.getCalendarList())
        return calendarPage
    }
}
