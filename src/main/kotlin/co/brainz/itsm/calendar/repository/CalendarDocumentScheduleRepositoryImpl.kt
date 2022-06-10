/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.calendar.repository

import co.brainz.itsm.calendar.entity.CalendarDocumentScheduleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CalendarDocumentScheduleRepositoryImpl :
    QuerydslRepositorySupport(CalendarDocumentScheduleEntity::class.java), CalendarDocumentScheduleRepositoryCustom {

}
