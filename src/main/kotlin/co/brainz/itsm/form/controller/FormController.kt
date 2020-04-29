package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest

/**
 * ### 폼(문서양식) 관련 화면 호출 처리용 클래스.
 *
 * FormController에서 처리하는 모든 호출은 View 혹은 View + Data를 반환한다.
 * 즉, View가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FormRestController에서 담당한다.
 *
 * @author Woo Dajung
 */
@Controller
@RequestMapping("/forms")
class FormController(private val formService: FormService) {

    private val formSearchPage: String = "form/formSearch"
    private val formListPage: String = "form/formList"
    private val formEditPage: String = "form/formEdit"
    private val formDesignerEditPage: String = "form/formDesignerEdit"
    private val formEditPreviewPage: String = "form/formEditPreview"

    /**
     * 폼 리스트 검색 호출 화면.
     */
    @GetMapping("/search")
    fun getFormSearch(request: HttpServletRequest, model: Model): String {
        return formSearchPage
    }

    /**
     * 폼 리스트 화면.
     */
    @GetMapping("/list")
    fun getFormList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search") ?: ""
        model.addAttribute("formList", formService.findForms(params))
        return formListPage
    }

    /**
     * 폼 기본 정보 등록 화면.
     */
    @GetMapping("/new")
    fun getFormNew(request: HttpServletRequest, model: Model): String {
        return formEditPage
    }

    /**
     * 폼 디자이너 편집 화면.
     */
    @GetMapping("/{formId}/edit")
    fun getFormDesignerEdit(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        return formDesignerEditPage
    }

    /**
     * 폼 디자이너 미리보기 화면.
     */
    @RequestMapping("/{formId}/preview", method = [RequestMethod.POST, RequestMethod.GET])
    fun getFormEditPreview(@PathVariable formId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("data", request.getParameter("data") ?: "")
        return formEditPreviewPage
    }

    /**
     * 폼 디자이너 상세화면.
     */
    @GetMapping("/{formId}/view")
    fun getFormDesignerView(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        model.addAttribute("isView", true)
        return formDesignerEditPage
    }
}
