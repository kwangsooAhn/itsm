/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import co.brainz.itsm.calendar.entity.QCalendarRepeatEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarRepeatRepositoryImpl :
    QuerydslRepositorySupport(CalendarRepeatEntity::class.java), CalendarRepeatRepositoryCustom {
    override fun findCalendarRepeatInCalendar(calendar: CalendarEntity): List<CalendarRepeatEntity> {
        val calendarRepeat = QCalendarRepeatEntity.calendarRepeatEntity
        return from(calendarRepeat)
            .where(calendarRepeat.calendar.eq(calendar))
            .fetch()
    }
}
