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
import co.brainz.itsm.calendar.entity.CalendarUserEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatDataEntity
import co.brainz.itsm.calendar.entity.CalendarUserRepeatEntity
import co.brainz.itsm.calendar.entity.CalendarUserScheduleEntity
import co.brainz.itsm.calendar.repository.CalendarUserRepeatDataRepository
import co.brainz.itsm.calendar.repository.CalendarUserRepeatRepository
import co.brainz.itsm.calendar.repository.CalendarUserScheduleRepository
import java.time.LocalDateTime
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CalendarUserScheduleService(
    private val currentSessionUser: CurrentSessionUser,
    private val calendarUserScheduleRepository: CalendarUserScheduleRepository,
    private val calendarUserRepeatService: CalendarUserRepeatService,
    private val calendarUserRepeatRepository: CalendarUserRepeatRepository,
    private val calendarUserRepeatDataRepository: CalendarUserRepeatDataRepository
) {

    /**
     * 일반 일정 조회
     */
    fun getUserScheduleList(calendarUser: CalendarUserEntity, range: Range): List<ScheduleData> {
        val scheduleList = mutableListOf<ScheduleData>()
        val schedules = calendarUserScheduleRepository.findCalendarScheduleByCalendarBetweenStartDtAndEndDt(calendarUser, range)
        schedules.forEach {
            scheduleList.add(
                ScheduleData(
                    id = it.scheduleId,
                    index = 1,
                    title = it.scheduleTitle,
                    contents = it.scheduleContents,
                    allDayYn = it.allDayYn,
                    ownerName = calendarUser.owner.userName,
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
        calendarUser: CalendarUserEntity,
        scheduleData: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendarSchedule = calendarUserScheduleRepository.save(
            CalendarUserScheduleEntity(
                scheduleId = "",
                calendar = calendarUser,
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
    fun putCalendarSchedule(calendarUser: CalendarUserEntity, scheduleData: ScheduleData): ZResponseConstants.STATUS {
        return when (scheduleData.repeatYn) {
            true -> putScheduleToRepeat(calendarUser, scheduleData)
            false -> this.putSchedule(calendarUser, scheduleData)
        }
    }

    /**
     * 스케줄 삭제
     */
    fun deleteCalendarSchedule(
        calendarUser: CalendarUserEntity,
        scheduleId: String
    ) {
        calendarUserScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendarUser, scheduleId)
    }

    /**
     * Excel 일괄 등록
     */
    fun postTemplateUpload(
        calendarUser: CalendarUserEntity,
        multipartFiles: List<MultipartFile>
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val userScheduleList = mutableListOf<CalendarUserScheduleEntity>()
        try {
            multipartFiles.forEach { file ->
                val workbook: Workbook = XSSFWorkbook(file.inputStream)
                val sheet = workbook.getSheetAt(0)
                val iterator = sheet.rowIterator()
                while (iterator.hasNext()) {
                    val row = iterator.next()
                    if (row.rowNum > 0) { // 0 은 제목 라인
                        if (this.isValidCheck(row)) {
                            println(row.getCell(0).dateCellValue)
                            println(row.getCell(0).localDateTimeCellValue)
                            userScheduleList.add(
                                CalendarUserScheduleEntity(
                                    allDayYn = false,
                                    startDt = this.getTimezoneToUTC(row.getCell(0).localDateTimeCellValue),
                                    endDt = this.getTimezoneToUTC(row.getCell(1).localDateTimeCellValue),
                                    scheduleTitle = row.getCell(2).stringCellValue,
                                    scheduleContents = row.getCell(3).stringCellValue,
                                    calendar = calendarUser,
                                    createDt = LocalDateTime.now()
                                )
                            )
                        }
                    }
                }
            }
            if (userScheduleList.isNotEmpty()) {
                calendarUserScheduleRepository.saveAll(userScheduleList)
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 엑셀 Cell 필수값 체크
     */
    private fun isValidCheck(row: Row): Boolean {
        var isValid = true
        for (i in 0..2) { // 내용은 제외
            if (isValid && row.getCell(i) == null) {
                isValid = false
            }
        }
        return isValid
    }

    /**
     * UTC 값을 사용자의 시간대로 변경
     */
    private fun getTimezoneToUTC(localDateTime: LocalDateTime): LocalDateTime {
        return calendarUserRepeatService.getTimezoneToUTC(localDateTime, currentSessionUser.getTimezone())
    }

    /**
     * 일반 일정 수정 (일반 > 반복)
     */
    private fun putScheduleToRepeat(
        calendarUser: CalendarUserEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        calendarUserScheduleRepository.deleteCalendarScheduleEntityByCalendarAndScheduleId(calendarUser, data.id)
        val repeat = calendarUserRepeatRepository.save(
            CalendarUserRepeatEntity(
                calendar = calendarUser
            )
        )
        if (repeat.repeatId.isNotEmpty()) {
            calendarUserRepeatDataRepository.save(
                CalendarUserRepeatDataEntity(
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
        calendarUser: CalendarUserEntity,
        data: ScheduleData
    ): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendarSchedule = calendarUserScheduleRepository.findById(data.id)
        if (calendarSchedule.isPresent) {
            calendarUserScheduleRepository.save(
                CalendarUserScheduleEntity(
                    scheduleId = data.id,
                    calendar = calendarUser,
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
