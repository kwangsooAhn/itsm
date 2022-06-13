package co.brainz.itsm.calendar.service

import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.calendar.constants.CalendarConstants
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarData
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.dto.CalendarRequest
import co.brainz.itsm.calendar.dto.CalendarResponse
import co.brainz.itsm.calendar.dto.Range
import co.brainz.itsm.calendar.dto.ScheduleData
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CalendarService(
    private val aliceMessageSource: AliceMessageSource,
    private val calendarUserService: CalendarUserService,
    private val calendarDocumentService: CalendarDocumentService,
    private val calendarUserScheduleService: CalendarUserScheduleService,
    private val calendarUserRepeatService: CalendarUserRepeatService,
    private val currentSessionUser: CurrentSessionUser,
    private val excelComponent: ExcelComponent
) {

    /**
     * 캘린더 목록 조회
     */
    fun getCalendarList(): List<CalendarDto> {
        val calendarList = mutableListOf<CalendarDto>()
        // user
        calendarList.addAll(
            calendarUserService.getCalendarUserList(
                CalendarCondition(
                    userKey = currentSessionUser.getUserKey(),
                    calendarType = CalendarConstants.CalendarType.USER.code
                )
            )
        )
        // document
        calendarList.addAll(
            calendarDocumentService.getCalendarDocumentList(
                CalendarCondition(
                    calendarType = CalendarConstants.CalendarType.DOCUMENT.code
                )
            )
        )
        return calendarList
    }

    /**
     * 캘린더별 전체 데이터 조회
     */
    fun getCalendars(calendarRequest: CalendarRequest): CalendarResponse {
        val calendarList = mutableListOf<CalendarData>()
        val range = this.getRange(calendarRequest, currentSessionUser.getTimezone())
        val utcRange = this.getRange(calendarRequest, "UTC")
        // 사용자 캘린더
        calendarList.addAll(calendarUserService.getUserCalendars(calendarRequest, range, utcRange))
        // 문서 캘린더
        calendarList.addAll(calendarDocumentService.getDocumentCalendars(range))
        return CalendarResponse(
            from = range.from.toString(),
            to = range.to.toString(),
            calendars = calendarList
        )
    }

    /**
     * 캘린더 일정 Excel 다운로드
     */
    fun getCalendarExcelDownload(calendarRequest: CalendarRequest): ResponseEntity<ByteArray> {
        val calendarDataList = this.getCalendars(calendarRequest).calendars
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.type"), cellWidth = 3000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.title"), cellWidth = 10000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.startDt"), cellWidth = 6000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.endDt"), cellWidth = 6000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.contents"), cellWidth = 15000)
                            )
                        )
                    )
                )
            )
        )
        calendarDataList.forEach { calendarData ->
            var calendarName = ""
            when (calendarData.type) {
                CalendarConstants.CalendarType.USER.code -> {
                    val calendar = calendarUserService.findCalendarInOwner(calendarData.id, currentSessionUser.getUserKey())
                    if (calendar.isPresent) {
                        calendarName = calendar.get().calendarName
                    }
                }
                CalendarConstants.CalendarType.DOCUMENT.code -> {
                    val calendar = calendarDocumentService.findCalendar(calendarData.id)
                    if (calendar.isPresent) {
                        calendarName = calendar.get().calendarName
                    }
                }
            }
            val scheduleList = mutableListOf<ScheduleData>()
            excelVO.sheets[0].sheetName = calendarName
            scheduleList.addAll(calendarData.schedules)
            scheduleList.addAll(calendarData.repeats)
            scheduleList.sortBy { it.startDt }
            scheduleList.forEach { schedule ->
                excelVO.sheets[0].rows.add(
                    ExcelRowVO(
                        cells = listOf(
                            ExcelCellVO(value = calendarName),
                            ExcelCellVO(value = schedule.title),
                            ExcelCellVO(value = this.getTimezoneFormat(schedule.startDt)),
                            ExcelCellVO(value = this.getTimezoneFormat(schedule.endDt)),
                            ExcelCellVO(value = schedule.contents)
                        )
                    )
                )
            }

        }
        return excelComponent.download(excelVO)
    }

    /**
     * 일괄 등록 템플릿 다운로드
     */
    fun getCalendarExcelTemplateDownload(): ResponseEntity<ByteArray> {
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.startDt"), cellWidth = 6000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.endDt"), cellWidth = 6000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.title"), cellWidth = 10000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("calendar.excel.contents"), cellWidth = 15000)
                            )
                        )
                    )
                )
            )
        )
        return excelComponent.download(excelVO)
    }

    /**
     * Excel 일괄 등록
     */
    fun postTemplateUpload(calendarId: String, multipartFiles: List<MultipartFile>): ZResponse {
        val calendar = calendarUserService.findCalendarInOwner(calendarId, currentSessionUser.getUserKey())
        val status = if (calendar.isPresent) {
            calendarUserScheduleService.postTemplateUpload(calendar.get(), multipartFiles)
        } else {
            ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Excel 다운로드시 사용자 타임존으로 변경
     */
    private fun getTimezoneFormat(localDateTime: LocalDateTime): String {
        return calendarUserRepeatService.getUTCToTimezone(localDateTime, currentSessionUser.getTimezone())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
