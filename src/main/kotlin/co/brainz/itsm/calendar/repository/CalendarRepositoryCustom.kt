/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarEntity
import java.util.Optional

interface CalendarRepositoryCustom : AliceRepositoryCustom {

    fun getCalendarList(userKey: String): List<CalendarDto>
    fun findCalendarInOwner(calendarId: String, userKey: String): Optional<CalendarEntity>
    fun findCalendarsInOwner(calendarIds: Set<String>, userKey: String): List<CalendarEntity>
}
