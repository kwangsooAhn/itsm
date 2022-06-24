/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserScheduleEntity

interface CalendarUserScheduleRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarScheduleByCalendarBetweenStartDtAndEndDt(
        calendarUser: CalendarUserEntity,
        range: Range
    ): List<CalendarUserScheduleEntity>
}
