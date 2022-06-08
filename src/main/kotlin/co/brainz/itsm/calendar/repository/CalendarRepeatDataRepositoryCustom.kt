/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity

interface CalendarRepeatDataRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarRepeatDataInRange(repeat: CalendarRepeatEntity): List<CalendarRepeatDataEntity>
    fun findCalendarRepeatDataAfterEndDt(repeatData: CalendarRepeatDataEntity): List<CalendarRepeatDataEntity>
}
