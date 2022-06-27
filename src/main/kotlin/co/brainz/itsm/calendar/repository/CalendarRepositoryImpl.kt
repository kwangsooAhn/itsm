package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.QCalendarEntity
import java.util.Optional
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarRepositoryImpl :
    QuerydslRepositorySupport(CalendarEntity::class.java), CalendarRepositoryCustom {
    override fun findCalendarByCalendarType(calendarType: String): Optional<CalendarEntity> {
        val calendar = QCalendarEntity.calendarEntity
        return Optional.ofNullable(
            from(calendar)
                .where(calendar.calendarType.eq(calendarType))
                .fetchOne()
        )
    }
}
