/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserRepeatCustomDataEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUserRepeatCustomDataRepository :
    JpaRepository<CalendarUserRepeatCustomDataEntity, String>, CalendarUserRepeatCustomDataRepositoryCustom {
    fun deleteAllByRepeatData(repeatData: CalendarUserRepeatDataEntity)
    fun findAllByRepeatData(repeatData: CalendarUserRepeatDataEntity): List<CalendarUserRepeatCustomDataEntity>
    fun deleteAllByRepeatDataAndDataIndex(repeatData: CalendarUserRepeatDataEntity, index: Int)
    fun deleteByRepeatDataAndDataIndex(repeatData: CalendarUserRepeatDataEntity, index: Int)
}
