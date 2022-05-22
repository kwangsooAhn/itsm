package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarRepeatDataRepositoryImpl :
    QuerydslRepositorySupport(CalendarRepeatDataEntity::class.java), CalendarRepeatDataRepositoryCustom {
}
