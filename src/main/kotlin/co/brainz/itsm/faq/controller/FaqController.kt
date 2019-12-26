package co.brainz.itsm.faq.controller

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
class FaqController(private val faqService: FaqService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val faqSearchPagePath: String = "faq/faqSearch"
    private val faqEditPagePath: String = "faq/faqEdit"
    private val faqListPagePath: String = "faq/faqList"
    private val faqViewPagePath: String = "faq/faqView"

    /**
     * FAQ 검색 화면 호출 처리
     */
    @GetMapping("/search")
    fun getFaqSearch(request: HttpServletRequest, model: Model): String {
        return faqSearchPagePath
    }

    /**
     * FAQ 신규 등록 화면 호출 처리
     */

    @GetMapping("/new")
    fun getFaqNew(request: HttpServletRequest, model: Model): String {
        return faqEditPagePath
    }

    /**
     * FAQ 검색 결과 리스트 화면 호출 처리
     */

    @GetMapping("/list")
    fun getFaqList(request: HttpServletRequest, model: Model): String {
        model.addAttribute("faqs", faqService.findAll())
        return faqListPagePath
    }

    /**
     * FAQ 상세 조회 화면 호출 처리
     */

    @GetMapping("/{faqId}/view")
    fun getFaqView(@PathVariable faqId: String, model: Model): String {
        model.addAttribute("faq", faqService.findOne(faqId))
        return faqViewPagePath
    }

    /**
     * FAQ 수정 화면 호출 처리
     */

    @GetMapping("/{faqId}/edit")
    fun getFaqEdit(@PathVariable faqId: String, model: Model): String {
        if (faqId != "") {
            model.addAttribute("faq", faqService.findOne(faqId))
        }
        return faqEditPagePath
    }
}
