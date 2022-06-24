/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.QCalendarEntity
import co.brainz.itsm.calendar.entity.QCalendarUserEntity
import com.querydsl.core.types.Projections
import java.util.Optional
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarUserRepositoryImpl : QuerydslRepositorySupport(CalendarUserEntity::class.java), CalendarUserRepositoryCustom {
    override fun getCalendarUserList(calendarCondition: CalendarCondition): List<CalendarDto> {
        val calendar = QCalendarEntity.calendarEntity
        val calendarUser = QCalendarUserEntity.calendarUserEntity
        return from(calendarUser)
            .select(
                Projections.constructor(
                    CalendarDto::class.java,
                    calendarUser.calendarId,
                    calendar.calendarType,
                    calendarUser.calendarName,
                    calendarUser.owner.userKey
                )
            )
            .innerJoin(calendar).on(calendar.calendarId.eq(calendarUser.calendarId))
            .where(calendar.calendarId.eq(calendarUser.calendarId))
            .where(calendar.calendarType.eq(calendarCondition.calendarType))
            .where(calendarUser.owner.userKey.eq(calendarCondition.userKey))
            .orderBy(calendarUser.calendarName.asc())
            .fetch()
    }

    override fun findCalendarInOwner(calendarId: String, userKey: String): Optional<CalendarUserEntity> {
        val calendarUser = QCalendarUserEntity.calendarUserEntity
        return Optional.ofNullable(
            from(calendarUser)
                .where(calendarUser.calendarId.eq(calendarId))
                .where(calendarUser.owner.userKey.eq(userKey))
                .fetchOne()
        )
    }

    override fun findCalendarsInOwner(calendarIds: Set<String>, userKey: String): List<CalendarUserEntity> {
        val calendarUser = QCalendarUserEntity.calendarUserEntity
        return from(calendarUser)
            .where(calendarUser.calendarId.`in`(calendarIds))
            .where(calendarUser.owner.userKey.eq(userKey))
            .fetch()
    }
}
