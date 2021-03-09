package co.brainz.itsm.notice.controller

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
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

    // Notice insert
    @PostMapping("/", "")
    fun insertNotice(@RequestBody noticeDto: NoticeDto) {
        noticeService.insertNotice(noticeDto)
    }

    // Notice update
    @PutMapping("/{noticeId}")
    fun updateNotice(@PathVariable noticeId: String, @RequestBody noticeDto: NoticeDto) {
        noticeService.updateNotice(noticeId, noticeDto)
    }

    // Notice delete
    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: String) {
        noticeService.delete(noticeId)
    }
}
