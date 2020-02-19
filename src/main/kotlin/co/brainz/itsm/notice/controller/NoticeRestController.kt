package co.brainz.itsm.notice.controller

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/notices")
class NoticeRestController(private val noticeService: NoticeService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    //Notice insert
    @PostMapping("/", "")
    fun insertNotice(@RequestBody noticeDto: NoticeDto) {
        noticeService.insertNotice(noticeDto)
    }

    //Notice update
    @PutMapping("/{noticeId}")
    fun updateNotice(@RequestBody noticeDto: NoticeDto) {
        noticeService.updateNotice(noticeDto)
    }

    //Notice delete
    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: String) {
        noticeService.delete(noticeId)
    }

    //공지사항 세부 조회
    @GetMapping("/{noticeId}")
    fun getNotice(@PathVariable noticeId: String): NoticeDto {
        return noticeService.findNoticeByNoticeNo(noticeId)
    }

    //공지사항 리스트 데이터 조회
    @GetMapping("/", "")
    fun getNoticeList(): MutableList<NoticeListDto> {
        return noticeService.findNoticeList()
    }
}
