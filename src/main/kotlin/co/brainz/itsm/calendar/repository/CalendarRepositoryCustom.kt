/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.calendar.dto.CalendarDto

interface CalendarRepositoryCustom : AliceRepositoryCustom {

    fun getCalendarList(userKey: String): List<CalendarDto>
}
