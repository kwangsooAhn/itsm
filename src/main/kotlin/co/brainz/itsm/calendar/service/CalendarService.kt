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
import co.brainz.itsm.calendar.entity.CalendarRepeatCustomDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity
import co.brainz.itsm.calendar.repository.CalendarRepeatCustomDataRepository
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
    private val calendarRepeatDataRepository: CalendarRepeatDataRepository,
    private val calendarRepeatCustomDataRepository: CalendarRepeatCustomDataRepository
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

                // 반복 일정 시작, 종료 안일 경우 진행한다. (특정 시점 이후 변경되면 그 날부터는 다른 list에서 실행)
                var repeatStartDt = this.getUTCToTimezone(repeatData.repeatStartDt, currentSessionUser.getTimezone())
                var repeatEndDt = repeatData.repeatEndDt?.let { this.getUTCToTimezone(it, currentSessionUser.getTimezone()) }

                // 현재 범위가 반복 일정의 최초 시작일보다 이후인지 체크
                var index = 1
                while (startDt <= range.to) {
                    if (repeatStartDt <= range.to) {
                        if (repeatEndDt != null) {
                            if (repeatEndDt < startDt) { // 반복 종료가 존재할 경우 다음 생성을 종료 (해당일자부터 수정된 반복 일정이 존재)
                                break;
                            }
                        }
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
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        if (calendar.isPresent) {
            // 일반 > 반복 일정
            if (!data.repeatYn) {
                // 일반 삭제
                calendarScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar.get(), data.id)
                // 반복 일정 데이터를 신규로 저장한다.

                // repeat 등록
                val calendarRepeat =
                    calendarRepeatRepository.save(
                        CalendarRepeatEntity(
                            calendar = calendar.get()
                        )
                    )
                // repeat 데이터 등록
                val repeatData = calendarRepeatDataRepository.save(
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
                when (data.repeatPeriod) {
                    CalendarConstants.RepeatPeriod.THIS.code -> {
                        // repeat, repeatData 신규 등록, custom 등록
                        CalendarRepeatCustomDataEntity(
                            customId = "",
                            repeatData = repeatData,
                            dataIndex = 1,
                            customType = CalendarConstants.CustomType.MODIFY.code,
                            scheduleTitle = data.title,
                            scheduleContents = data.contents,
                            allDayYn = data.allDayYn,
                            startDt = data.startDt,
                            endDt = data.endDt
                        )

                    }
                    CalendarConstants.RepeatPeriod.AFTER.code -> {
                        // 추가 없음
                    }
                    CalendarConstants.RepeatPeriod.ALL.code -> {
                        // 추가없음
                    }
                }
            } else {
                when (data.repeatPeriod) {
                    CalendarConstants.RepeatPeriod.THIS.code -> {
                        // dataId 로 custom data 에 등록한다.
                        // 조회시 커스텀 데이터 존재하면.. 그걸로 대체한다. (수정은 대체... 삭제는 제외)
                        if (data.dataId != null && data.index != null) {
                            val repeatData = calendarRepeatDataRepository.findByDataId(data.dataId)
                            // 기존에 수정된 데이터면 삭제한다.
                            calendarRepeatCustomDataRepository.deleteAllByRepeatDataAndDataIndex(repeatData, data.index)
                            // 새롭게 넣는다.
                            CalendarRepeatCustomDataEntity(
                                customId = "",
                                repeatData = repeatData,
                                dataIndex = data.index,
                                customType = CalendarConstants.CustomType.MODIFY.code,
                                scheduleTitle = data.title,
                                scheduleContents = data.contents,
                                allDayYn = data.allDayYn,
                                startDt = data.startDt,
                                endDt = data.endDt
                            )
                        }
                    }
                    CalendarConstants.RepeatPeriod.AFTER.code -> {
                        // data 테이블에 새롭게 추가
                        // 조회시 해당 날짜부터는 새롭게 추가된 정보로 나와야한다.
                        // dataId 로 기존의 repeatEndDt 를 신규 날짜 전날로 업데이트한 후 신규로 추가
                        val repeat = calendarRepeatRepository.findByRepeatId(data.id)
                        if (data.dataId != null) {
                            val repeatData = calendarRepeatDataRepository.findByDataId(data.dataId)

                            // 현재 선택된 반복일정을 종료날짜하고 이후에 추가된 반복일정을 제거한다.
                            val currentRepeatData = calendarRepeatDataRepository.save(
                                CalendarRepeatDataEntity(
                                    dataId = data.dataId,
                                    repeat = repeat.get(),
                                    scheduleTitle = repeatData.scheduleTitle,
                                    scheduleContents = repeatData.scheduleContents,
                                    startDt = repeatData.startDt,
                                    endDt = repeatData.endDt,
                                    allDayYn = repeatData.allDayYn,
                                    repeatStartDt = repeatData.startDt,
                                    repeatEndDt = data.startDt.with(LocalTime.of(0, 0, 0)).minusSeconds(1),
                                    repeatValue = repeatData.repeatValue,
                                    repeatType = repeatData.repeatType
                                )
                            )
                            val afterRepeatDataList = calendarRepeatDataRepository.findCalendarRepeatDataAfterEndDt(currentRepeatData)
                            afterRepeatDataList.forEach {
                                calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                                calendarRepeatDataRepository.deleteByDataId(it.dataId)
                            }

                            calendarRepeatDataRepository.save(
                                CalendarRepeatDataEntity(
                                    repeat = repeat.get(),
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
                    }
                    CalendarConstants.RepeatPeriod.ALL.code -> {
                        // 반복 일정이 다수 등록될 수 있으므로 삭제 후 새롭게 추가한다.
                        val repeat = calendarRepeatRepository.findByRepeatId(data.id)
                        val calendarDataList = calendarRepeatDataRepository.findAllByRepeat(repeat.get())
                        calendarDataList.forEach {
                            calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                        }
                        calendarRepeatDataRepository.deleteAllByRepeat(repeat.get())
                        calendarRepeatDataRepository.save(
                            CalendarRepeatDataEntity(
                                repeat = repeat.get(),
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
                    }
                }
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
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
        val repeat = calendarRepeatRepository.findByRepeatId(calendarDeleteRequest.id)
        if (repeat.isPresent) {
            when (calendarDeleteRequest.repeatPeriod) {
                CalendarConstants.RepeatPeriod.ALL.code -> {
                    val calendarDataList = calendarRepeatDataRepository.findAllByRepeat(repeat.get())
                    calendarDataList.forEach {
                        calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                    }
                    calendarRepeatDataRepository.deleteAllByRepeat(repeat.get())
                    calendarRepeatRepository.delete(repeat.get())
                }
                CalendarConstants.RepeatPeriod.THIS.code -> {
                    // 커스텀에 오늘을 삭제로 추가한다.
                    //
                }
                CalendarConstants.RepeatPeriod.AFTER.code -> {
                    // 데이터에 신규로 추가한다
                }
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }

        return ZResponse(
            status = status.code
        )
    }
}
