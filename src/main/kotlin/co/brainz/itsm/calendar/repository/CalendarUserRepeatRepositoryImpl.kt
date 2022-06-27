/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity
import co.brainz.itsm.calendar.entity.QCalendarUserRepeatEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarUserRepeatRepositoryImpl :
    QuerydslRepositorySupport(CalendarUserRepeatEntity::class.java), CalendarUserRepeatRepositoryCustom {
    override fun findCalendarRepeatInCalendar(calendarUser: CalendarUserEntity): List<CalendarUserRepeatEntity> {
        val calendarUserRepeat = QCalendarUserRepeatEntity.calendarUserRepeatEntity
        return from(calendarUserRepeat)
            .where(calendarUserRepeat.calendar.eq(calendarUser))
            .fetch()
    }
}
