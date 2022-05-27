/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarRepeatDataRepository : JpaRepository<CalendarRepeatDataEntity, String>, CalendarRepeatDataRepositoryCustom {
    fun deleteAllByRepeat(repeat: CalendarRepeatEntity)
    fun findAllByRepeat(repeat: CalendarRepeatEntity): List<CalendarRepeatDataEntity>
    fun findByDataId(dataId: String): CalendarRepeatDataEntity
}
