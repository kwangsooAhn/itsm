/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.service.FaqService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * ### FAQ 관련 뷰 화면 호출 처리용 클래스.
 *
 * @author Jung heechan
 * @see co.brainz.itsm.faq.controller.FaqRestController
 */
@Controller
@RequestMapping("/faqs")
class FaqController(private val faqService: FaqService, private val codeService: CodeService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val faqSearchPage: String = "faq/faqSearch"
    private val faqEditPage: String = "faq/faqEdit"
    private val faqListPage: String = "faq/faqList"
    private val faqViewPage: String = "faq/faqView"

    /**
     * FAQ 검색 화면 호출 처리
     */
    @GetMapping("/search")
    fun getFaqSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("faqGroupList", faqService.findAllFaqGroups())
        return faqSearchPage
    }

    /**
     * FAQ 신규 등록 화면 호출 처리
     */
    @GetMapping("/new")
    fun getFaqNew(request: HttpServletRequest, model: Model): String {
        model.addAttribute("faqGroupList", codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE))
        return faqEditPage
    }

    /**
     * FAQ 검색 결과 리스트 화면 호출 처리
     */
    @GetMapping("")
    fun getFaqs(faqSearchCondition: FaqSearchCondition, model: Model): String {
        val result = faqService.getFaqs(faqSearchCondition)
        model.addAttribute("faqs", result.data)
        model.addAttribute("paging", result.paging)
        return faqListPage
    }

    /**
     * FAQ 상세 조회 화면 호출 처리
     */
    @GetMapping("/{faqId}/view")
    fun getFaqView(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faqGroupList", faqService.findAllFaqGroups())
        model.addAttribute("faq", faqService.getFaqDetail(faqId))
        return faqViewPage
    }

    /**
     * FAQ 수정 화면 호출 처리
     */
    @GetMapping("/{faqId}/edit")
    fun getFaqEdit(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faqGroupList", codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE))
        if (faqId != "") {
            model.addAttribute("faq", faqService.getFaqDetail(faqId))
        }
        return faqEditPage
    }
}
