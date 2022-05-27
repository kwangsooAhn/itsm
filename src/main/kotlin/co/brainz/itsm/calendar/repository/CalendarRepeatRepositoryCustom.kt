/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity

interface CalendarRepeatRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarRepeatInCalendar(calendar: CalendarEntity): List<CalendarRepeatEntity>
}
