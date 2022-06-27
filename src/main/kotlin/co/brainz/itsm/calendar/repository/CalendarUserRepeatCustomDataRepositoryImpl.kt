/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarUserRepeatCustomDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarUserRepeatCustomDataRepositoryImpl :
    QuerydslRepositorySupport(CalendarUserRepeatCustomDataEntity::class.java), CalendarUserRepeatCustomDataRepositoryCustom
