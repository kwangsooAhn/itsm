/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserScheduleEntity
import co.brainz.itsm.calendar.entity.QCalendarUserScheduleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarUserScheduleRepositoryImpl :
    QuerydslRepositorySupport(CalendarUserScheduleEntity::class.java), CalendarUserScheduleRepositoryCustom {
    override fun findCalendarScheduleByCalendarBetweenStartDtAndEndDt(
        calendarUser: CalendarUserEntity,
        range: Range
    ): List<CalendarUserScheduleEntity> {
        val userSchedule = QCalendarUserScheduleEntity.calendarUserScheduleEntity
        return from(userSchedule)
            .where(userSchedule.calendar.eq(calendarUser))
            .where(userSchedule.startDt.between(range.from, range.to))
            .fetch()
    }
}
