/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.service.NoticeService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
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
    fun insertNotice(@RequestBody noticeDto: NoticeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(noticeService.insertNotice(noticeDto))
    }

    // Notice update
    @PutMapping("/{noticeNo}")
    fun updateNotice(@PathVariable noticeNo: String, @RequestBody noticeDto: NoticeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(noticeService.updateNotice(noticeNo, noticeDto))
    }

    // Notice delete
    @DeleteMapping("/{noticeNo}")
    fun deleteNotice(@PathVariable noticeNo: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(noticeService.delete(noticeNo))
    }
}
