/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.constants.CalendarConstants
import co.brainz.itsm.calendar.dto.CalendarData
import co.brainz.itsm.calendar.dto.CalendarDeleteRequest
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.dto.CalendarRequest
import co.brainz.itsm.calendar.dto.CalendarResponse
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.dto.ScheduleData
import co.brainz.itsm.calendar.repository.CalendarRepeatRepository
import co.brainz.itsm.calendar.repository.CalendarRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import javax.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CalendarService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarRepository: CalendarRepository,
    private val calendarRepeatRepository: CalendarRepeatRepository,
    private val calendarScheduleService: CalendarScheduleService,
    private val calendarRepeatService: CalendarRepeatService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 캘린더 목록 조회
     */
    fun getCalendarList(): List<CalendarDto> {
        return calendarRepository.getCalendarList(currentSessionUser.getUserKey())
    }

    /**
     * 캘린더별 전체 데이터 조회
     */
    fun getCalendars(calendarRequest: CalendarRequest): CalendarResponse {
        val calendars =
            calendarRepository.findCalendarsInOwner(calendarRequest.calendarIds.toSet(), currentSessionUser.getUserKey())
        val calendarList = mutableListOf<CalendarData>()
        val range = this.getRange(calendarRequest, currentSessionUser.getTimezone())
        val utcRange = this.getRange(calendarRequest, "UTC")
        calendars.forEach { calendar ->
            calendarList.add(
                CalendarData(
                    id = calendar.calendarId,
                    schedules = calendarScheduleService.getScheduleList(calendar, range),
                    repeats = calendarRepeatService.getRepeatMiningList(calendar, utcRange, calendarRequest)
                )
            )
        }
        return CalendarResponse(
            from = range.from.toString(),
            to = range.to.toString(),
            calendars = calendarList
        )
    }

    /**
     * 일반 일정 등록
     */
    @Transactional
    fun postCalendarSchedule(calendarId: String, scheduleData: ScheduleData): ZResponse {
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarScheduleService.postCalendarSchedule(calendar.get(), scheduleData)
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
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarScheduleService.putCalendarSchedule(calendar.get(), scheduleData)
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
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            calendarScheduleService.deleteCalendarSchedule(calendar.get(), calendarDeleteRequest.id)
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
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarRepeatService.postCalendarRepeat(calendar.get(), data)
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
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarRepeatService.putCalendarRepeat(calendar.get(), data)
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
        val repeat = calendarRepeatRepository.findByRepeatId(calendarDeleteRequest.id)
        if (repeat.isPresent) {
            status = calendarRepeatService.deleteCalendarRepeat(repeat.get(), calendarDeleteRequest)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 조회 범위 설정
     */
    private fun getRange(calendarRequest: CalendarRequest, zoneId: String): Range {
        lateinit var startDate: LocalDateTime
        lateinit var endDate: LocalDateTime
        val standardArray = calendarRequest.standard.split("-")
        when (calendarRequest.viewType) {
            CalendarConstants.ViewType.MONTH.code -> {
                val standardDate =
                    LocalDateTime.of(standardArray[0].toInt(), standardArray[1].toInt(), 1, 0, 0)
                startDate = standardDate.minusDays(standardDate[WeekFields.SUNDAY_START.dayOfWeek()].toLong() - 1)
                endDate = startDate.plusDays(41).with(LocalTime.of(23, 59, 59)) // 1일부터 + 41일 까지 조회
            }
            CalendarConstants.ViewType.WEEK.code -> {
                val standardDate =
                    LocalDateTime.of(standardArray[0].toInt(), standardArray[1].toInt(), standardArray[2].toInt(), 0, 0)
                startDate = standardDate.minusDays(standardDate[WeekFields.SUNDAY_START.dayOfWeek()].toLong() - 1)
                endDate =
                    standardDate.plusDays(
                        7 - standardDate[WeekFields.SUNDAY_START.dayOfWeek()].toLong()
                    ).with(LocalTime.of(23, 59, 59))
            }
            CalendarConstants.ViewType.DAY.code -> {
                val standardDate =
                    LocalDateTime.of(standardArray[0].toInt(), standardArray[1].toInt(), standardArray[2].toInt(), 0, 0)
                startDate = standardDate
                endDate = standardDate.with(LocalTime.of(23, 59, 59))
            }
            CalendarConstants.ViewType.TASK.code -> {
                startDate = LocalDateTime.of(standardArray[0].toInt(), standardArray[1].toInt(), 1, 0, 0)
                endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.of(23, 59, 59))
            }
        }

        return Range(
            from = LocalDateTime.ofInstant(startDate.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC")),
            to = LocalDateTime.ofInstant(endDate.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC"))
        )
    }
}
