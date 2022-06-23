/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUserRepeatDataRepository : JpaRepository<CalendarUserRepeatDataEntity, String>, CalendarUserRepeatDataRepositoryCustom {
    fun deleteAllByRepeat(repeat: CalendarUserRepeatEntity)
    fun deleteByDataId(dataId: String)
    fun findAllByRepeat(repeat: CalendarUserRepeatEntity): List<CalendarUserRepeatDataEntity>
    fun findByDataId(dataId: String): CalendarUserRepeatDataEntity
}
