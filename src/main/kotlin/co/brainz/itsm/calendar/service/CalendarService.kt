/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.repository.CalendarRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CalendarService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarRepository: CalendarRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun getCalendars(): List<CalendarDto> {
        return calendarRepository.getCalendars(currentSessionUser.getUserKey())
    }


}
