/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity

interface CalendarScheduleRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarScheduleByCalendarBetweenStartDtAndEndDt(calendar: CalendarEntity, range: Range): List<CalendarScheduleEntity>
}
