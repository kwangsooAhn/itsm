/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarScheduleRepository : JpaRepository<CalendarScheduleEntity, String>, CalendarScheduleRepositoryCustom {
    fun deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar: CalendarEntity, scheduleId: String)
}
