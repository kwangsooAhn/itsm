package co.brainz.itsm.calendar.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.itsm.calendar.constants.CalendarConstants
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarData
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.entity.CalendarDocumentEntity
import co.brainz.itsm.calendar.entity.CalendarDocumentScheduleEntity
import co.brainz.itsm.calendar.repository.CalendarDocumentRepository
import co.brainz.itsm.calendar.repository.CalendarDocumentScheduleRepository
import co.brainz.itsm.calendar.repository.CalendarRepository
import co.brainz.itsm.instance.dto.InstanceScheduleDto
import co.brainz.workflow.instance.repository.WfInstanceRepository
import java.time.LocalDateTime
import java.util.Optional
import org.springframework.stereotype.Service

@Service
class CalendarDocumentService(
    private val calendarRepository: CalendarRepository,
    private val calendarDocumentRepository: CalendarDocumentRepository,
    private val calendarDocumentScheduleRepository: CalendarDocumentScheduleRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    /**
     * 문서 캘린더 목록 조회
     */
    fun getCalendarDocumentList(calendarCondition: CalendarCondition): List<CalendarDto> {
        return calendarDocumentRepository.getCalendarDocumentList(calendarCondition)
    }

    /**
     * 문서 캘린더 스케줄 조회
     */
    fun getDocumentCalendars(): List<CalendarData> {
        val calendarList = mutableListOf<CalendarData>()
        return calendarList
    }

    /**
     * 문서 캘린더 스케줄 등록
     */
    fun postDocumentSchedule(instanceScheduleDto: InstanceScheduleDto): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        // 문서형 캘린더는 1개로 현재 고정이므로 별도의 전달을 받지 않는다.
        val calendar = calendarRepository.findCalendarByCalendarType(CalendarConstants.CalendarType.DOCUMENT.code)
        val instance = wfInstanceRepository.findByInstanceId(instanceScheduleDto.instanceId)
        if (calendar.isPresent && instance != null) {
            val calendarDocument = calendarDocumentRepository.findCalendar(calendar.get().calendarId)
            if (calendarDocument.isPresent) {
                // 문서 정보로 테이블 데이터 저장
                calendarDocumentScheduleRepository.save(
                    CalendarDocumentScheduleEntity(
                        calendar = calendarDocument.get(),
                        instance = instance,
                        scheduleTitle = instanceScheduleDto.title,
                        scheduleContents = instanceScheduleDto.contents,
                        startDt = instanceScheduleDto.startDt,
                        endDt = instanceScheduleDto.endDt,
                        allDayYn = instanceScheduleDto.allDayYn,
                        createDt = LocalDateTime.now()
                    )
                )
            } else {
                status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return status
    }

    /**
     * 문서 캘린더 스케줄 삭제
     */
    fun deleteDocumentSchedule(instanceId: String, scheduleId: String): ZResponseConstants.STATUS {
        var status = ZResponseConstants.STATUS.SUCCESS
        val calendar = calendarRepository.findCalendarByCalendarType(CalendarConstants.CalendarType.DOCUMENT.code)
        val instance = wfInstanceRepository.findByInstanceId(instanceId)
        if (calendar.isPresent && instance != null) {
            calendarDocumentScheduleRepository.deleteById(scheduleId)
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return status
    }

    /**
     * 문서 캘린더 조회
     */
    fun findCalendar(calendarId: String): Optional<CalendarDocumentEntity> {
        return calendarDocumentRepository.findCalendar(calendarId)
    }
}
