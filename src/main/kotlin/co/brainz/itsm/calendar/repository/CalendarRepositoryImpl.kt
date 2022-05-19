/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.QCalendarEntity
import com.querydsl.core.types.Projections
import java.util.Optional
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarRepositoryImpl : QuerydslRepositorySupport(CalendarEntity::class.java), CalendarRepositoryCustom {
    override fun getCalendarList(userKey: String): List<CalendarDto> {
        val calendar = QCalendarEntity.calendarEntity
        return from(calendar)
            .select(
                Projections.constructor(
                    CalendarDto::class.java,
                    calendar.calendarId,
                    calendar.calendarName,
                    calendar.owner.userKey
                )
            )
            .where(calendar.owner.userKey.eq(userKey))
            .orderBy(calendar.calendarName.asc())
            .fetch()
    }

    override fun findCalendarInOwner(calendarId: String, userKey: String): Optional<CalendarEntity> {
        val calendar = QCalendarEntity.calendarEntity
        return Optional.ofNullable(
            from(calendar)
                .where(calendar.calendarId.eq(calendarId))
                .where(calendar.owner.userKey.eq(userKey))
                .fetchOne()
        )
    }

    override fun findCalendarsInOwner(calendarIds: Set<String>, userKey: String): List<CalendarEntity> {
        val calendar = QCalendarEntity.calendarEntity
        return from(calendar)
            .where(calendar.calendarId.`in`(calendarIds))
            .where(calendar.owner.userKey.eq(userKey))
            .fetch()
    }
}
