/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity

interface CalendarRepeatDataRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarRepeatDataInRange(repeat: CalendarRepeatEntity, range: Range): List<CalendarRepeatDataEntity>
}
