/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.dto.ScheduleData
import co.brainz.itsm.calendar.entity.CalendarEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarRepeatEntity
import co.brainz.itsm.calendar.entity.CalendarScheduleEntity
import co.brainz.itsm.calendar.repository.CalendarRepeatDataRepository
import co.brainz.itsm.calendar.repository.CalendarRepeatRepository
import co.brainz.itsm.calendar.repository.CalendarScheduleRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class CalendarScheduleService(
    private val calendarScheduleRepository: CalendarScheduleRepository,
    private val calendarRepeatRepository: CalendarRepeatRepository,
    private val calendarRepeatDataRepository: CalendarRepeatDataRepository
) {

    /**
     * 일반 일정 조회
     */
    fun getScheduleList(calendar: CalendarEntity, range: Range): List<ScheduleData> {
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
     * 일반 일정 등록
     */
    fun postCalendarSchedule(
        calendar: CalendarEntity,
        scheduleData: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendarSchedule = calendarScheduleRepository.save(
            CalendarScheduleEntity(
                scheduleId = "",
                calendar = calendar,
                scheduleTitle = scheduleData.title,
                scheduleContents = scheduleData.contents,
                allDayYn = scheduleData.allDayYn,
                startDt = scheduleData.startDt,
                endDt = scheduleData.endDt,
                createDt = LocalDateTime.now()
            )
        )
        if (calendarSchedule.scheduleId.isEmpty()) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 일반 일정 수정 (일반 > 일반, 일반 > 반복)
     */
    fun putCalendarSchedule(calendar: CalendarEntity, scheduleData: ScheduleData): ZResponseConstants.STATUS {
        return when (scheduleData.repeatYn) {
            true -> putScheduleToRepeat(calendar, scheduleData)
            false -> this.putSchedule(calendar, scheduleData)
        }
    }

    /**
     * 스케줄 삭제
     */
    fun deleteCalendarSchedule(
        calendar: CalendarEntity,
        scheduleId: String
    ) {
        calendarScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar, scheduleId)
    }

    /**
     * 일반 일정 수정 (일반 > 반복)
     */
    private fun putScheduleToRepeat(
        calendar: CalendarEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        calendarScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendar, data.id)
        val repeat = calendarRepeatRepository.save(
            CalendarRepeatEntity(
                calendar = calendar
            )
        )
        if (repeat.repeatId.isNotEmpty()) {
            calendarRepeatDataRepository.save(
                CalendarRepeatDataEntity(
                    dataId = "",
                    repeatTitle = data.title,
                    repeatContents = data.contents,
                    startDt = data.startDt,
                    endDt = data.endDt,
                    allDayYn = data.allDayYn,
                    repeat = repeat,
                    repeatStartDt = data.startDt,
                    repeatType = data.repeatType,
                    repeatValue = data.repeatValue,
                    createDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 일반 일정 수정 (일반 > 일반)
     */
    private fun putSchedule(
        calendar: CalendarEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendarSchedule = calendarScheduleRepository.findById(data.id)
        if (calendarSchedule.isPresent) {
            calendarScheduleRepository.save(
                CalendarScheduleEntity(
                    scheduleId = data.id,
                    calendar = calendar,
                    scheduleTitle = data.title,
                    scheduleContents = data.contents,
                    allDayYn = data.allDayYn,
                    startDt = data.startDt,
                    endDt = data.endDt,
                    createDt = calendarSchedule.get().createDt,
                    updateDt = LocalDateTime.now()
                )
            )
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return status
    }
}
