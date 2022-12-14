/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.faq.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.service.FaqService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ### FAQ 관련 데이터 조회 처리용 클래스.
 *
 * FaqRestController는 REST API형식의 호출을 처리한다.
 *
 * @author Jung heechan
 * @see co.brainz.itsm.faq.controller.FaqController
 */
@RestController
@RequestMapping("/rest/faqs")
class FaqRestController(private val faqService: FaqService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * FAQ 1건 상세 조회
     */
    @GetMapping("/{faqId}")
    fun getFaq(request: HttpServletRequest, @PathVariable faqId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(faqService.getFaqDetail(faqId))
    }

    /**
     * 신규 FAQ 등록 처리
     */
    @PostMapping("/", "")
    fun insertFaq(@RequestBody faqDto: FaqDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(faqService.createFaq(faqDto))
    }

    /**
     * FAQ 수정 처리
     */
    @PutMapping("/{faqId}")
    fun updateFaq(@PathVariable faqId: String, @RequestBody faqDto: FaqDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(faqService.updateFaq(faqId, faqDto))
    }

    /**
     * FAQ 삭제 처리
     */
    @DeleteMapping("/{faqId}")
    fun deleteFaq(@PathVariable faqId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(faqService.deleteFaq(faqId))
    }
}
