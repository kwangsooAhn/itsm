/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import co.brainz.itsm.calendar.entity.QCalendarRepeatDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarRepeatDataRepositoryImpl :
    QuerydslRepositorySupport(CalendarRepeatDataEntity::class.java), CalendarRepeatDataRepositoryCustom {
    override fun findCalendarRepeatDataInRange(repeat: CalendarRepeatEntity, range: Range): List<CalendarRepeatDataEntity> {
        val calendarRepeatData = QCalendarRepeatDataEntity.calendarRepeatDataEntity
        return from(calendarRepeatData)
            .where(calendarRepeatData.repeat.eq(repeat))
            .fetch()
    }

    override fun findCalendarRepeatDataAfterEndDt(repeatData: CalendarRepeatDataEntity): List<CalendarRepeatDataEntity> {
        val calendarRepeatData = QCalendarRepeatDataEntity.calendarRepeatDataEntity
        return from(calendarRepeatData)
            .where(calendarRepeatData.repeat.eq(repeatData.repeat))
            .where(calendarRepeatData.startDt.gt(repeatData.endDt))
            .fetch()
    }
}
