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
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity
import co.brainz.itsm.calendar.repository.CalendarRepeatDataRepository
import co.brainz.itsm.calendar.repository.CalendarRepeatRepository
import co.brainz.itsm.calendar.repository.CalendarRepository
import co.brainz.itsm.calendar.repository.CalendarScheduleRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CalendarService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarRepository: CalendarRepository,
    private val calendarScheduleRepository: CalendarScheduleRepository,
    private val calendarRepeatRepository: CalendarRepeatRepository,
    private val calendarRepeatDataRepository: CalendarRepeatDataRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

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
        val range = Range(
            from = calendarRequest.from,
            to = calendarRequest.to
        )
        val calendarList = mutableListOf<CalendarData>()
        calendars.forEach { calendar ->
            // 일반 스케줄
            val scheduleList = mutableListOf<ScheduleData>()
            val schedules = calendarScheduleRepository.findCalendarScheduleByCalendarBetweenStartDtAndEndDt(calendar, range)
            schedules.forEach {
                scheduleList.add(
                    ScheduleData(
                        id = it.scheduleId,
                        index = 1,
                        title = it.scheduleTitle,
                        contents = it.scheduleContents,
                        allDayYn = it.allDayYn,
                        ownerName = calendar.owner.userName,
                        startDt = it.startDt,
                        endDt = it.endDt
                    )
                )
            }
            // TODO: 반복 스케줄
            val repeatList = mutableListOf<ScheduleData>()

            calendarList.add(
                CalendarData(
                    id = calendar.calendarId,
                    schedules = scheduleList,
                    repeats = repeatList
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
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            calendarScheduleRepository.save(
                CalendarScheduleEntity(
                    scheduleId = "",
                    calendar = calendar.get(),
                    scheduleTitle = scheduleData.title,
                    scheduleContents = scheduleData.contents,
                    allDayYn = scheduleData.allDayYn,
                    startDt = scheduleData.startDt,
                    endDt = scheduleData.endDt
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
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
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            if (scheduleData.repeatYn) { // 반복 일정으로 변경된 경우
                // 일반 삭제
                calendarScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar.get(), scheduleData.id)
                // 반복 일정 등록
                val repeat = calendarRepeatRepository.save(
                    CalendarRepeatEntity(
                        repeatId = "",
                        calendar = calendar.get()
                    )
                )
                calendarRepeatDataRepository.save(
                    CalendarRepeatDataEntity(
                        dataId = "",
                        scheduleTitle = scheduleData.title,
                        scheduleContents = scheduleData.contents,
                        startDt = scheduleData.startDt,
                        endDt = scheduleData.endDt,
                        allDayYn = scheduleData.allDayYn,
                        repeat = repeat,
                        repeatStartDt = scheduleData.startDt,
                        repeatType = scheduleData.repeatType,
                        repeatValue = scheduleData.repeatValue
                    )
                )
            } else { // 그대로 일반인 경우
                calendarScheduleRepository.save(
                    CalendarScheduleEntity(
                        scheduleId = scheduleData.id,
                        calendar = calendar.get(),
                        scheduleTitle = scheduleData.title,
                        scheduleContents = scheduleData.contents,
                        allDayYn = scheduleData.allDayYn,
                        startDt = scheduleData.startDt,
                        endDt = scheduleData.endDt
                    )
                )
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 스케줄 삭제
     */
    @Transactional
    fun deleteCalendarSchedule(calendarId: String, calendarDeleteRequest: CalendarDeleteRequest): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            calendarScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar.get(), calendarDeleteRequest.id)
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
    fun postCalendarRepeat(calendarId: String, data: ScheduleData): ZResponse {
        return ZResponse()
    }

    /**
     * 반복 일정 수정
     */
    @Transactional
    fun putCalendarRepeat(calendarId: String, data: ScheduleData): ZResponse {
        return ZResponse()
    }

    /**
     * 반복 일정 삭제
     */
    @Transactional
    fun deleteCalendarRepeat(calendarId: String, calendarDeleteRequest: CalendarDeleteRequest): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        // 반복 일정 삭제는 period 에 따라 삭제 대상이 달라진다.
        // all 이면.. repeat, data, custom 모두 삭제
        // today 면... custom 에 존재하면 삭제.. custom 에 없으면 삭제로 추가
        // 오늘이후 면... data에 신규 추가 .. 신규 시작 날짜는 해당 일로 지정
        when (calendarDeleteRequest.repeatPeriod) {
            CalendarConstants.RepeatPeriod.ALL.code -> {

            }
            CalendarConstants.RepeatPeriod.THIS.code -> {

            }
            CalendarConstants.RepeatPeriod.AFTER.code -> {

            }
        }

        return ZResponse(
            status = status.code
        )
    }
}
