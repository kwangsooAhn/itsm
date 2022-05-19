/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity
import co.brainz.itsm.calendar.entity.QCalendarScheduleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarScheduleRepositoryImpl :
    QuerydslRepositorySupport(CalendarScheduleEntity::class.java), CalendarScheduleRepositoryCustom {
    override fun findCalendarScheduleByCalendarBetweenStartDtAndEndDt(
        calendar: CalendarEntity,
        range: Range
    ): List<CalendarScheduleEntity> {
        val schedule = QCalendarScheduleEntity.calendarScheduleEntity
        return from(schedule)
            .where(schedule.calendar.eq(calendar))
            .where(schedule.startDt.between(range.from, range.to))
            .fetch()
    }
}
