package co.brainz.itsm.calendar

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/calendars")
class CalendarController {
    private val calendarPage: String = "calendar/calendar"

    @GetMapping("")
    fun getCalendar(model: Model): String {
        return calendarPage
    }
}
