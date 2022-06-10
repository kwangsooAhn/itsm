/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity

interface CalendarUserRepeatRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarRepeatInCalendar(calendarUser: CalendarUserEntity): List<CalendarUserRepeatEntity>
}
