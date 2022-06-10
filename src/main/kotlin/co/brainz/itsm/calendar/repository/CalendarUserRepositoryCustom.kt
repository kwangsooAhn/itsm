/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import java.util.Optional

interface CalendarUserRepositoryCustom : AliceRepositoryCustom {
    fun getCalendarUserList(calendarCondition: CalendarCondition): List<CalendarDto>
    fun findCalendarInOwner(calendarId: String, userKey: String): Optional<CalendarUserEntity>
    fun findCalendarsInOwner(calendarIds: Set<String>, userKey: String): List<CalendarUserEntity>
}
