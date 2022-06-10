package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.entity.CalendarEntity
import java.util.Optional

interface CalendarRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarByCalendarType(calendarType: String): Optional<CalendarEntity>
}
