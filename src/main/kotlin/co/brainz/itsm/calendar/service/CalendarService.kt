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
import co.brainz.itsm.calendar.entity.CalendarEntity
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
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
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

    fun getRepeatData(
        repeat: CalendarRepeatEntity,
        repeatData: CalendarRepeatDataEntity,
        calendar: CalendarEntity,
        index: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): ScheduleData {
        return ScheduleData(
            id = repeat.repeatId,
            index = index,
            dataId = repeatData.dataId,
            title = repeatData.scheduleTitle,
            contents = repeatData.scheduleContents,
            allDayYn = repeatData.allDayYn,
            startDt = startDate,
            endDt = endDate,
            ownerName = calendar.owner.userName,
            repeatYn = true,
            repeatType = repeatData.repeatType,
            repeatValue = repeatData.repeatValue,
            repeatPeriod = repeatData.repeatType
        )
    }

    /**
     * 일반 일정 조회
     */
    private fun getScheduleList(calendar: CalendarEntity, range: Range): List<ScheduleData> {
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
        return scheduleList
    }

    /**
     * 반복 일정 데이터 생성
     */
    private fun getRepeatMiningList(calendar: CalendarEntity, range: Range): List<ScheduleData> {
        val repeatMiningList = mutableListOf<ScheduleData>()
        val repeatList = calendarRepeatRepository.findCalendarRepeatInCalendar(calendar)
        repeatList.forEach { repeat ->
            val repeatDataList = calendarRepeatDataRepository.findCalendarRepeatDataInRange(repeat, range)
            repeatDataList.forEach { repeatData ->
                var startDt = this.getUTCToTimezone(repeatData.startDt, currentSessionUser.getTimezone())
                var endDt = this.getUTCToTimezone(repeatData.endDt, currentSessionUser.getTimezone())
                val duration = Duration.between(startDt, endDt)
                // 현재 범위가 반복 일정의 최초 시작일보다 이후인지 체크
                var index = 1
                while (startDt <= range.to) {
                    if ((startDt >= range.from && startDt <= range.to) || (endDt >= range.from && endDt <= range.to)) {
                        repeatMiningList.add(
                            this.getRepeatData(
                                repeat,
                                repeatData,
                                calendar,
                                index,
                                startDt,
                                endDt
                            )
                        )
                        index++
                    }
                    startDt = this.getNextDate(startDt, repeatData)
                    endDt = startDt.plusSeconds(duration.seconds)
                }
            }
        }

        // Timezone -> UTC 변환
        repeatMiningList.forEach {
            it.startDt = this.getTimezoneToUTC(it.startDt, currentSessionUser.getTimezone())
            it.endDt = this.getTimezoneToUTC(it.endDt, currentSessionUser.getTimezone())
        }

        return repeatMiningList
    }

    /**
     * 반복 일정 다음 날짜 구하기
     */
    private fun getNextDate(
        localDateTime: LocalDateTime,
        repeatData: CalendarRepeatDataEntity
    ): LocalDateTime {
        return when (repeatData.repeatType) {
            CalendarConstants.RepeatType.WEEK_OF_MONTH.code -> {
                localDateTime.plusDays(7)
            }
            CalendarConstants.RepeatType.DAY_OF_WEEK_IN_MONTH.code -> {
                if (repeatData.repeatValue != null) {
                    val values = repeatData.repeatValue.toString().split("_")
                    val dayOfWeekInMonth =
                        localDateTime.plusMonths(1).with(
                            TemporalAdjusters.dayOfWeekInMonth(
                                values[0].toInt(),
                                DayOfWeek.of(values[1].toInt())
                            )
                        )
                    dayOfWeekInMonth.with(LocalTime.of(localDateTime.hour, localDateTime.minute, localDateTime.second))
                } else {
                    localDateTime.plusDays(7)
                }
            }
            else -> localDateTime
        }
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
                    schedules = this.getScheduleList(calendar, range),
                    repeats = this.getRepeatMiningList(calendar, utcRange)
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
        }

        return Range(
            from = LocalDateTime.ofInstant(startDate.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC")),
            to = LocalDateTime.ofInstant(endDate.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC"))
        )
    }

    /**
     * UTC 시간으로 변경
     */
    private fun getTimezoneToUTC(localDateTime: LocalDateTime, zoneId: String): LocalDateTime {
        return LocalDateTime.ofInstant(localDateTime.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC"))
    }

    /**
     * Timezone 시간으로 변경
     */
    private fun getUTCToTimezone(localDateTime: LocalDateTime, zoneId: String): LocalDateTime {
        return LocalDateTime.ofInstant(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of(zoneId))
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
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            val calendarRepeat =
                calendarRepeatRepository.save(
                    CalendarRepeatEntity(
                        calendar = calendar.get()
                    )
                )
            calendarRepeatDataRepository.save(
                CalendarRepeatDataEntity(
                    repeat = calendarRepeat,
                    scheduleTitle = data.title,
                    scheduleContents = data.contents,
                    startDt = data.startDt,
                    endDt = data.endDt,
                    repeatStartDt = data.startDt,
                    allDayYn = data.allDayYn,
                    repeatValue = data.repeatValue,
                    repeatType = data.repeatType
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
