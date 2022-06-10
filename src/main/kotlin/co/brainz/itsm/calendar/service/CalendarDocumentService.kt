package co.brainz.itsm.calendar.service

import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.calendar.dto.CalendarCondition
import co.brainz.itsm.calendar.dto.CalendarData
import co.brainz.itsm.calendar.dto.CalendarDto
import co.brainz.itsm.calendar.dto.ScheduleData
import co.brainz.itsm.calendar.entity.CalendarDocumentEntity
import co.brainz.itsm.calendar.repository.CalendarDocumentRepository
import java.util.Optional
import org.springframework.stereotype.Service

@Service
class CalendarDocumentService(
    private val calendarDocumentRepository: CalendarDocumentRepository
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
    fun postDocumentSchedule(data: ScheduleData): Any {
        return ZResponse()
    }

    /**
     * 문서 캘린더 스케줄 수정
     */
    fun putDocumentSchedule(data: ScheduleData) {

    }

    /**
     * 문서 캘린더 스케줄 삭제
     */
    fun deleteDocumentSchedule() {

    }

    /**
     * 문서 캘린더 조회
     */
    fun findCalendar(calendarId: String): Optional<CalendarDocumentEntity> {
        return calendarDocumentRepository.findCalendar(calendarId)
    }
}
