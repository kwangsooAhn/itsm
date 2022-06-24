/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.constants.CalendarConstants
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarData
import co.brainz.itsm.calendar.dto.CalendarDeleteRequest
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.dto.CalendarRequest
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.dto.ScheduleData
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.repository.CalendarUserRepeatRepository
import co.brainz.itsm.calendar.repository.CalendarUserRepository
import java.time.LocalDateTime
import java.util.Optional
import javax.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CalendarUserService(
    private val aliceMessageSource: AliceMessageSource,
    private val currentSessionUser: CurrentSessionUser,
    private val calendarUserRepository: CalendarUserRepository,
    private val calendarUserRepeatRepository: CalendarUserRepeatRepository,
    private val calendarUserScheduleService: CalendarUserScheduleService,
    private val calendarUserRepeatService: CalendarUserRepeatService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 캘린더 목록 조회
     */
    fun getCalendarUserList(calendarCondition: CalendarCondition): List<CalendarDto> {
        return calendarUserRepository.getCalendarUserList(calendarCondition)
    }

    fun getUserCalendars(
        calendarRequest: CalendarRequest,
        range: Range,
        utcRange: Range
    ): List<CalendarData> {
        val calendarList = mutableListOf<CalendarData>()
        val calendars =
            calendarUserRepository.findCalendarsInOwner(calendarRequest.calendarIds.toSet(), currentSessionUser.getUserKey())
        calendars.forEach { calendar ->
            calendarList.add(
                CalendarData(
                    id = calendar.calendarId,
                    type = CalendarConstants.CalendarType.USER.code,
                    schedules = calendarUserScheduleService.getUserScheduleList(calendar, range),
                    repeats = calendarUserRepeatService.getRepeatMiningList(calendar, utcRange, calendarRequest)
                )
            )
        }
        return calendarList
    }

    /**
     * 일반 일정 등록
     */
    @Transactional
    fun postCalendarSchedule(calendarId: String, scheduleData: ScheduleData): ZResponse {
        val calendar = calendarUserRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarUserScheduleService.postCalendarSchedule(calendar.get(), scheduleData)
        } else {
            ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 일반 일정 수정
     */
    @Transactional
    fun putCalendarSchedule(calendarId: String, scheduleData: ScheduleData): ZResponse {
        val calendar = calendarUserRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarUserScheduleService.putCalendarSchedule(calendar.get(), scheduleData)
        } else {
            ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 스케줄 삭제
     */
    @Transactional
    fun deleteCalendarSchedule(
        calendarId: String,
        calendarDeleteRequest: CalendarDeleteRequest
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarUserRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            calendarUserScheduleService.deleteCalendarSchedule(calendar.get(), calendarDeleteRequest.id)
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 반복 일정 등록
     */
    @Transactional
    fun postCalendarRepeat(
        calendarId: String,
        data: ScheduleData
    ): ZResponse {
        val calendar = calendarUserRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarUserRepeatService.postCalendarRepeat(calendar.get(), data)
        } else {
            ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 반복 일정 수정
     */
    @Transactional
    fun putCalendarRepeat(
        calendarId: String,
        data: ScheduleData
    ): ZResponse {
        val calendar = calendarUserRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarUserRepeatService.putCalendarRepeat(calendar.get(), data)
        } else {
            ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 반복 일정 삭제
     */
    @Transactional
    fun deleteCalendarRepeat(
        calendarId: String,
        calendarDeleteRequest: CalendarDeleteRequest
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val repeat = calendarUserRepeatRepository.findByRepeatId(calendarDeleteRequest.id)
        if (repeat.isPresent) {
            status = calendarUserRepeatService.deleteCalendarRepeat(repeat.get(), calendarDeleteRequest)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 사용자의 캘린더 조회
     */
    fun findCalendarInOwner(calendarId: String, owner: String): Optional<CalendarUserEntity> {
        return calendarUserRepository.findCalendarInOwner(calendarId, owner)
    }

    /**
     * 사용자 캘린더 추가
     */
    fun setCalendarUser(calendar: CalendarEntity, user: AliceUserEntity) {
        calendarUserRepository.save(
            CalendarUserEntity(
                calendarId = calendar.calendarId,
                calendarName = aliceMessageSource.getMessage("calendar.label.default"),
                owner = user,
                createDt = LocalDateTime.now()
            )
        )
    }
}
