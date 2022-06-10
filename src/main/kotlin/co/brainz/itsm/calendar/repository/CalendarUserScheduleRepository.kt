/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserScheduleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUserScheduleRepository : JpaRepository<CalendarUserScheduleEntity, String>, CalendarUserScheduleRepositoryCustom {
    fun deleteCalendarScheduleEntityByCalendarAndScheduleId(calendarUser: CalendarUserEntity, scheduleId: String)
}
