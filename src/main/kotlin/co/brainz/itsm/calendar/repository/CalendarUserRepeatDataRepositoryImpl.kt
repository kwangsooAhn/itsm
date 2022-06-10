/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity
import co.brainz.itsm.calendar.entity.QCalendarUserRepeatDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarUserRepeatDataRepositoryImpl :
    QuerydslRepositorySupport(CalendarUserRepeatDataEntity::class.java), CalendarUserRepeatDataRepositoryCustom {
    override fun findCalendarRepeatDataInRange(repeat: CalendarUserRepeatEntity): List<CalendarUserRepeatDataEntity> {
        val calendarUserRepeatData = QCalendarUserRepeatDataEntity.calendarUserRepeatDataEntity
        return from(calendarUserRepeatData)
            .where(calendarUserRepeatData.repeat.eq(repeat))
            .fetch()
    }

    override fun findCalendarRepeatDataAfterEndDt(repeatData: CalendarUserRepeatDataEntity): List<CalendarUserRepeatDataEntity> {
        val calendarUserRepeatData = QCalendarUserRepeatDataEntity.calendarUserRepeatDataEntity
        return from(calendarUserRepeatData)
            .where(calendarUserRepeatData.repeat.eq(repeatData.repeat))
            .where(calendarUserRepeatData.startDt.gt(repeatData.endDt))
            .fetch()
    }
}
