/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarRepeatCustomDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarRepeatCustomDataRepository :
    JpaRepository<CalendarRepeatCustomDataEntity, String>, CalendarRepeatCustomDataRepositoryCustom {
    fun deleteAllByRepeatData(repeatData: CalendarRepeatDataEntity)
    fun findAllByRepeatData(repeatData: CalendarRepeatDataEntity): List<CalendarRepeatCustomDataEntity>
    fun deleteAllByRepeatDataAndDataIndex(repeatData: CalendarRepeatDataEntity, index: Int)
    fun deleteByRepeatDataAndDataIndex(repeatData: CalendarRepeatDataEntity, index: Int)
}
