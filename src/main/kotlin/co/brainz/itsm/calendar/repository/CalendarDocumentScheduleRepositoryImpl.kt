/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.CalendarDocumentCondition
import co.brainz.itsm.calendar.entity.CalendarDocumentScheduleEntity
import co.brainz.itsm.calendar.entity.QCalendarDocumentScheduleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarDocumentScheduleRepositoryImpl :
    QuerydslRepositorySupport(CalendarDocumentScheduleEntity::class.java), CalendarDocumentScheduleRepositoryCustom {
    override fun findDocumentSchedule(calendarDocumentCondition: CalendarDocumentCondition): List<CalendarDocumentScheduleEntity> {
        val documentSchedule = QCalendarDocumentScheduleEntity.calendarDocumentScheduleEntity
        val query = from(documentSchedule)
        if (calendarDocumentCondition.instanceId != null) {
            query.where(documentSchedule.instance.instanceId.eq(calendarDocumentCondition.instanceId))
        }
        if (calendarDocumentCondition.range != null) {
            query.where(documentSchedule.startDt.between(calendarDocumentCondition.range.from, calendarDocumentCondition.range.to))
            query.where(documentSchedule.endDt.between(calendarDocumentCondition.range.from, calendarDocumentCondition.range.to))
        }
        query.orderBy(documentSchedule.startDt.asc())
        return query.fetch()
    }
}
