/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.entity.CalendarUserRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity

interface CalendarUserRepeatDataRepositoryCustom : AliceRepositoryCustom {
    fun findCalendarRepeatDataInRange(repeat: CalendarUserRepeatEntity): List<CalendarUserRepeatDataEntity>
    fun findCalendarRepeatDataAfterEndDt(repeatData: CalendarUserRepeatDataEntity): List<CalendarUserRepeatDataEntity>
}
