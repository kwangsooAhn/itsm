/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarDocumentEntity
import java.util.Optional

interface CalendarDocumentRepositoryCustom : AliceRepositoryCustom {
    fun getCalendarDocumentList(calendarCondition: CalendarCondition): List<CalendarDto>
    fun findCalendar(calendarId: String): Optional<CalendarDocumentEntity>
}
