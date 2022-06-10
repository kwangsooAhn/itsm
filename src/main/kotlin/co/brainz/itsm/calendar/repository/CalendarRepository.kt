package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarRepository : JpaRepository<CalendarEntity, String>, CalendarRepositoryCustom
