/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUserRepeatRepository : JpaRepository<CalendarUserRepeatEntity, String>, CalendarUserRepeatRepositoryCustom {
    fun findByRepeatId(repeatId: String): Optional<CalendarUserRepeatEntity>
}
