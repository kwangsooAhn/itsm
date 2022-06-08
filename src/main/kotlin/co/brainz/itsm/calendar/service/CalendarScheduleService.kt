/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.util.CurrentSessionUser
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
import java.time.format.DateTimeFormatter
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CalendarScheduleService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarScheduleRepository: CalendarScheduleRepository,
    private val calendarRepeatService: CalendarRepeatService,
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
     * Excel 일괄 등록
     */
    fun postTemplateUpload(
        calendar: CalendarEntity,
        multipartFiles: List<MultipartFile>
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val scheduleList = mutableListOf<CalendarScheduleEntity>()
        try {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            multipartFiles.forEach { file ->
                val workbook: Workbook = XSSFWorkbook(file.inputStream)
                val sheet = workbook.getSheetAt(0)
                val iterator = sheet.rowIterator()
                while (iterator.hasNext()) {
                    val row = iterator.next()
                    if (row.rowNum > 0) { // 0 은 제목 라인
                        scheduleList.add(
                            CalendarScheduleEntity(
                                allDayYn = false,
                                startDt = this.getTimezoneToUTC(LocalDateTime.parse(row.getCell(0).stringCellValue, format)),
                                endDt = this.getTimezoneToUTC(LocalDateTime.parse(row.getCell(1).stringCellValue, format)),
                                scheduleTitle = row.getCell(2).stringCellValue,
                                scheduleContents = row.getCell(3).stringCellValue,
                                calendar = calendar,
                                createDt = LocalDateTime.now()
                            )
                        )
                    }
                }
            }
            if (scheduleList.isNotEmpty()) {
                calendarScheduleRepository.saveAll(scheduleList)
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * UTC 값을 사용자의 시간대로 변경
     */
    private fun getTimezoneToUTC(localDateTime: LocalDateTime): LocalDateTime {
        return calendarRepeatService.getTimezoneToUTC(localDateTime, currentSessionUser.getTimezone())
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
