package co.brainz.itsm.faq.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
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
 * FaqController에서 처리하는 모든 호출은 View 혹은 View + Data를 반환한다.
 * 즉, View가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FaqRestController에서 담당한다.
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
    @GetMapping("/list")
    fun getFaqs(faqSearchRequestDto: FaqSearchRequestDto, model: Model): String {
        model.addAttribute("faqs", faqService.getFaqs(faqSearchRequestDto))
        return faqListPage
    }

    /**
     * FAQ 상세 조회 화면 호출 처리
     */
    @GetMapping("/{faqId}/view")
    fun getFaqView(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faqGroupList", faqService.findAllFaqGroups())
        model.addAttribute("faq", faqService.findOne(faqId))
        return faqViewPage
    }

    /**
     * FAQ 수정 화면 호출 처리
     */
    @GetMapping("/{faqId}/edit")
    fun getFaqEdit(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faqGroupList", codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE))
        if (faqId != "") {
            model.addAttribute("faq", faqService.findOne(faqId))
        }
        return faqEditPage
    }
}
