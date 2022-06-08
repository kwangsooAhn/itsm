/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.constants.CalendarConstants
import co.brainz.itsm.calendar.dto.CalendarDeleteRequest
import co.brainz.itsm.calendar.dto.CalendarRequest
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
import co.brainz.itsm.calendar.repository.CalendarScheduleRepository
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import org.springframework.stereotype.Service

@Service
class CalendarRepeatService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarRepeatRepository: CalendarRepeatRepository,
    private val calendarRepeatDataRepository: CalendarRepeatDataRepository,
    private val calendarRepeatCustomDataRepository: CalendarRepeatCustomDataRepository,
    private val calendarScheduleRepository: CalendarScheduleRepository
) {

    /**
     * 반복 일정 데이터 생성
     */
    fun getRepeatMiningList(calendar: CalendarEntity, range: Range, calendarRequest: CalendarRequest): List<ScheduleData> {
        val repeatMiningList = mutableListOf<ScheduleData>()
        val repeatList = calendarRepeatRepository.findCalendarRepeatInCalendar(calendar)
        repeatList.forEach { repeat ->
            val repeatDataMiningList = mutableListOf<ScheduleData>()
            val repeatDataList = calendarRepeatDataRepository.findCalendarRepeatDataInRange(repeat)
            repeatDataList.forEach { repeatData ->
                var startDt = this.getUTCToTimezone(repeatData.startDt, currentSessionUser.getTimezone())
                var endDt = this.getUTCToTimezone(repeatData.endDt, currentSessionUser.getTimezone())
                val duration = Duration.between(startDt, endDt)

                // 범위 내의 반복 일정 데이터 생성 (repeat_start_dt, repeat_end_dt)
                val repeatStartDt = this.getUTCToTimezone(repeatData.repeatStartDt, currentSessionUser.getTimezone())
                val repeatEndDt = repeatData.repeatEndDt?.let { this.getUTCToTimezone(it, currentSessionUser.getTimezone()) }

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
                            repeatDataMiningList.add(
                                ScheduleData(
                                    id = repeat.repeatId,
                                    index = index,
                                    dataId = repeatData.dataId,
                                    title = repeatData.repeatTitle,
                                    contents = repeatData.repeatContents,
                                    allDayYn = repeatData.allDayYn,
                                    startDt = startDt,
                                    endDt = endDt,
                                    ownerName = calendar.owner.userName,
                                    repeatYn = true,
                                    repeatType = repeatData.repeatType,
                                    repeatValue = repeatData.repeatValue,
                                    repeatPeriod = repeatData.repeatType
                                )
                            )
                        }
                        index++
                        startDt = this.getNextDate(startDt, repeatData)
                        endDt = startDt.plusSeconds(duration.seconds)
                    }
                }
            }

            // 커스텀 데이터 처리
            repeatDataList.forEach {
                val repeatCustomDataList =
                    calendarRepeatCustomDataRepository.findAllByRepeatData(it)
                // dataId, index 가 같은 데이터는 override
                repeatCustomDataList.forEach { customData ->
                    val iterator = repeatDataMiningList.listIterator()
                    while (iterator.hasNext()) {
                        val data = iterator.next()
                        if (data.dataId == customData.repeatData.dataId &&
                            data.index == customData.dataIndex
                        ) {
                            when (customData.customType) {
                                CalendarConstants.CustomType.MODIFY.code -> {
                                    data.title = customData.customTitle
                                    data.contents = customData.customContents
                                    data.startDt = customData.startDt!!
                                    data.endDt = customData.endDt!!
                                }
                                CalendarConstants.CustomType.DELETE.code -> {
                                    iterator.remove()
                                }
                            }
                        }
                    }
                }
            }
            repeatMiningList.addAll(repeatDataMiningList)
        }

        // Timezone -> UTC 변환
        repeatMiningList.forEach {
            it.startDt = this.getTimezoneToUTC(it.startDt, currentSessionUser.getTimezone())
            it.endDt = this.getTimezoneToUTC(it.endDt, currentSessionUser.getTimezone())
        }

        return repeatMiningList
    }

    /**
     * 반복 일정 등록
     */
    fun postCalendarRepeat(
        calendar: CalendarEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendarRepeat = calendarRepeatRepository.save(
            CalendarRepeatEntity(
                calendar = calendar
            )
        )
        if (calendarRepeat.repeatId.isNotEmpty()) {
            calendarRepeatDataRepository.save(
                CalendarRepeatDataEntity(
                    repeat = calendarRepeat,
                    repeatTitle = data.title,
                    repeatContents = data.contents,
                    startDt = data.startDt,
                    endDt = data.endDt,
                    repeatStartDt = data.startDt,
                    allDayYn = data.allDayYn,
                    repeatValue = data.repeatValue,
                    repeatType = data.repeatType,
                    createDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 반복 일정 수정 (반복 > 반복, 반복 > 일반)
     */
    fun putCalendarRepeat(
        calendar: CalendarEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        return when (data.repeatYn) {
            true -> putRepeat(data)
            false -> putRepeatToSchedule(calendar, data)
        }
    }

    /**
     * 반복 일정 삭제
     */
    fun deleteCalendarRepeat(
        repeat: CalendarRepeatEntity,
        request: CalendarDeleteRequest
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (request.repeatPeriod) {
            CalendarConstants.RepeatPeriod.ALL.code -> {
                this.deleteAllRepeat(repeat)
            }
            CalendarConstants.RepeatPeriod.TODAY.code -> {
                if (request.dataId != null) {
                    val repeatData = calendarRepeatDataRepository.findByDataId(request.dataId)
                    // 삭제전에 오늘만 별도로 저장된 경우가 있으면 제거한다.
                    calendarRepeatCustomDataRepository.deleteByRepeatDataAndDataIndex(
                        repeatData,
                        request.index
                    )
                    // 커스텀에 오늘을 삭제로 추가한다.
                    calendarRepeatCustomDataRepository.save(
                        CalendarRepeatCustomDataEntity(
                            repeatData = repeatData,
                            dataIndex = request.index,
                            allDayYn = false,
                            customTitle = "",
                            customContents = "",
                            customType = CalendarConstants.CustomType.DELETE.code,
                            createDt = LocalDateTime.now()
                        )
                    )
                }
            }
            CalendarConstants.RepeatPeriod.AFTER.code -> {
                if (request.dataId != null) {
                    val repeatData = calendarRepeatDataRepository.findByDataId(request.dataId)
                    // 기존 반복 일정의 완료일을 전날로 변경한다.
                    val currentRepeatData = calendarRepeatDataRepository.save(
                        CalendarRepeatDataEntity(
                            dataId = request.dataId,
                            repeat = repeat,
                            repeatTitle = repeatData.repeatTitle,
                            repeatContents = repeatData.repeatContents,
                            startDt = repeatData.startDt,
                            endDt = repeatData.endDt,
                            allDayYn = repeatData.allDayYn,
                            repeatStartDt = repeatData.startDt,
                            repeatEndDt = request.startDt?.with(LocalTime.of(0, 0, 0))?.minusSeconds(1),
                            repeatValue = repeatData.repeatValue,
                            repeatType = repeatData.repeatType
                        )
                    )
                    // 커스텀 데이터 삭제
                    calendarRepeatCustomDataRepository.deleteByRepeatDataAndDataIndex(
                        repeatData,
                        request.index
                    )
                    // repeatData 에 이후 일정이 있으면 삭제한다.
                    val afterRepeatDataList = calendarRepeatDataRepository.findCalendarRepeatDataAfterEndDt(currentRepeatData)
                    afterRepeatDataList.forEach {
                        calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                        calendarRepeatDataRepository.deleteByDataId(it.dataId)
                    }
                }
            }
            else -> status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return status
    }

    /**
     * 반복 일정 수정 (반복 > 일반)
     */
    private fun putRepeatToSchedule(calendar: CalendarEntity, data: ScheduleData): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val repeat = calendarRepeatRepository.findByRepeatId(data.id)
        if (repeat.isPresent) {
            // 반복 일정 관련 전부 삭제
            this.deleteAllRepeat(repeat.get())
            // 일반 등록
            calendarScheduleRepository.save(
                CalendarScheduleEntity(
                    scheduleId = "",
                    calendar = calendar,
                    scheduleTitle = data.title,
                    scheduleContents = data.contents,
                    allDayYn = data.allDayYn,
                    startDt = data.startDt,
                    endDt = data.endDt,
                    createDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return status
    }

    /**
     * 반복 일정 수정 (반복 > 반복)
     */
    private fun putRepeat(data: ScheduleData): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (data.repeatPeriod) {
            CalendarConstants.RepeatPeriod.TODAY.code -> {
                if (data.dataId != null && data.index != null) {
                    val repeatData = calendarRepeatDataRepository.findByDataId(data.dataId)
                    // 기존 커스텀에 저장된 데이터가 존재하면 삭제한다.
                    calendarRepeatCustomDataRepository.deleteAllByRepeatDataAndDataIndex(
                        repeatData,
                        data.index
                    )
                    calendarRepeatCustomDataRepository.save(
                        CalendarRepeatCustomDataEntity(
                            customId = "",
                            repeatData = repeatData,
                            dataIndex = data.index,
                            customType = CalendarConstants.CustomType.MODIFY.code,
                            customTitle = data.title,
                            customContents = data.contents,
                            allDayYn = data.allDayYn,
                            startDt = data.startDt,
                            endDt = data.endDt,
                            createDt = LocalDateTime.now()
                        )
                    )
                }
            }
            CalendarConstants.RepeatPeriod.AFTER.code -> {
                val repeat = calendarRepeatRepository.findByRepeatId(data.id)
                if (data.dataId != null) {
                    val repeatData = calendarRepeatDataRepository.findByDataId(data.dataId)
                    // 기존 커스텀 삭제
                    calendarRepeatCustomDataRepository.deleteAllByRepeatData(repeatData)
                    // 현재 선택된 반복일정을 종료날짜를 새롭게 추가된 날짜 -1초로 업데이트
                    val currentRepeatData = calendarRepeatDataRepository.save(
                        CalendarRepeatDataEntity(
                            dataId = data.dataId,
                            repeat = repeat.get(),
                            repeatTitle = repeatData.repeatTitle,
                            repeatContents = repeatData.repeatContents,
                            startDt = repeatData.startDt,
                            endDt = repeatData.endDt,
                            allDayYn = repeatData.allDayYn,
                            repeatStartDt = repeatData.startDt,
                            repeatEndDt = data.startDt.with(LocalTime.of(0, 0, 0)).minusSeconds(1),
                            repeatValue = repeatData.repeatValue,
                            repeatType = repeatData.repeatType
                        )
                    )
                    // 신규 반복 일정 이후 날짜에 등록된 반복 일정이 존재할 경우 삭제
                    val afterRepeatDataList = calendarRepeatDataRepository.findCalendarRepeatDataAfterEndDt(currentRepeatData)
                    afterRepeatDataList.forEach {
                        calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                        calendarRepeatDataRepository.deleteByDataId(it.dataId)
                    }
                    // 신규 반복 일정 추가
                    calendarRepeatDataRepository.save(
                        CalendarRepeatDataEntity(
                            repeat = repeat.get(),
                            repeatTitle = data.title,
                            repeatContents = data.contents,
                            startDt = data.startDt,
                            endDt = data.endDt,
                            repeatStartDt = data.startDt,
                            allDayYn = data.allDayYn,
                            repeatValue = data.repeatValue,
                            repeatType = data.repeatType,
                            createDt = LocalDateTime.now()
                        )
                    )
                } else {
                    status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
                }
            }
            CalendarConstants.RepeatPeriod.ALL.code -> {
                val repeat = calendarRepeatRepository.findByRepeatId(data.id)
                val repeatDataList = calendarRepeatDataRepository.findAllByRepeat(repeat.get())
                // 반복 일정이 다수 등록될 수 있으므로 삭제 후 새롭게 추가한다.
                repeatDataList.forEach {
                    calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
                }
                calendarRepeatDataRepository.deleteAllByRepeat(repeat.get())
                calendarRepeatDataRepository.save(
                    CalendarRepeatDataEntity(
                        repeat = repeat.get(),
                        repeatTitle = data.title,
                        repeatContents = data.contents,
                        startDt = data.startDt,
                        endDt = data.endDt,
                        repeatStartDt = data.startDt,
                        allDayYn = data.allDayYn,
                        repeatValue = data.repeatValue,
                        repeatType = data.repeatType,
                        createDt = LocalDateTime.now()
                    )
                )
            }
            else -> status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 반복 관련 데이터 전부 삭제
     */
    private fun deleteAllRepeat(repeat: CalendarRepeatEntity) {
        val repeatDataList = calendarRepeatDataRepository.findAllByRepeat(repeat)
        repeatDataList.forEach {
            calendarRepeatCustomDataRepository.deleteAllByRepeatData(it)
        }
        calendarRepeatDataRepository.deleteAllByRepeat(repeat)
        calendarRepeatRepository.delete(repeat)
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
     * UTC 시간으로 변경
     */
    fun getTimezoneToUTC(localDateTime: LocalDateTime, zoneId: String): LocalDateTime {
        return LocalDateTime.ofInstant(localDateTime.toInstant(ZoneId.of(zoneId).rules.getOffset(Instant.now())), ZoneId.of("UTC"))
    }

    /**
     * Timezone 시간으로 변경
     */
    fun getUTCToTimezone(localDateTime: LocalDateTime, zoneId: String): LocalDateTime {
        return LocalDateTime.ofInstant(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.of(zoneId))
    }
}
