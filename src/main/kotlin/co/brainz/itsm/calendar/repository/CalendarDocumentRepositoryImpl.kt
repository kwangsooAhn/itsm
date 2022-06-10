/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarDocumentEntity
import co.brainz.itsm.calendar.entity.QCalendarDocumentEntity
import co.brainz.itsm.calendar.entity.QCalendarEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import java.util.Optional
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarDocumentRepositoryImpl :
    QuerydslRepositorySupport(CalendarDocumentEntity::class.java), CalendarDocumentRepositoryCustom {
    override fun getCalendarDocumentList(calendarCondition: CalendarCondition): List<CalendarDto> {
        val calendar = QCalendarEntity.calendarEntity
        val calendarDocument = QCalendarDocumentEntity.calendarDocumentEntity
        return from(calendarDocument)
            .select(
                Projections.constructor(
                    CalendarDto::class.java,
                    calendarDocument.calendarId,
                    calendar.calendarType,
                    calendarDocument.calendarName,
                    Expressions.asString("")
                )
            )
            .innerJoin(calendar).on(calendar.calendarId.eq(calendarDocument.calendarId))
            .where(calendar.calendarId.eq(calendarDocument.calendarId))
            .where(calendar.calendarType.eq(calendarCondition.calendarType))
            .orderBy(calendarDocument.calendarName.asc())
            .fetch()
    }

    override fun findCalendar(calendarId: String): Optional<CalendarDocumentEntity> {
        val calendarDocument = QCalendarDocumentEntity.calendarDocumentEntity
        return Optional.ofNullable(
            from(calendarDocument)
                .where(calendarDocument.calendarId.eq(calendarId))
                .fetchOne()
        )
    }
}
