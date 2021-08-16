/**
 * 문서양식 관련 화면 호출 처리용 클래스.
 *
 * FormController 에서 처리하는 모든 호출은 View 혹은 View + Data 를 반환한다.
 * 즉, View 가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FormRestController 에서 담당한다.
 *
 * @author Woo Dajung
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.itsm.form.controller

import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.itsm.form.service.FormService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/forms")
class FormController(private val formService: FormService) {

    private val formSearchPage: String = "formDesigner/formSearch"
    private val formListPage: String = "formDesigner/formList"
    private val formEditPreviewPage: String = "formDesigner/formEditPreview"
    private val formDesignerEditPage: String = "formDesigner/formDesigner"

    /**
     * 문서양식 리스트 검색 화면.
     *
     * @param request
     * @param model
     * @return String
     */
    @GetMapping("/search")
    fun getFormSearch(request: HttpServletRequest, model: Model): String {
        return formSearchPage
    }

    /**
     * 문서양식 리스트 화면.
     *
     * @param request
     * @param isScroll
     * @param model
     * @return String
     */
    @GetMapping("")
    fun getFormList(formSearchCondition: FormSearchCondition, model: Model): String {
        val result = formService.findForms(formSearchCondition)
        model.addAttribute("formList", result)
        model.addAttribute("paging", result.paging)
        return formListPage
    }

    /**
     * 문서양식 디자이너 편집 화면.
     *
     * @param formId
     * @param model
     * @return String
     */
    @GetMapping("/{formId}/edit")
    fun getFormDesignerEdit(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        return formDesignerEditPage
    }

    /**
     * 문서양식 디자이너 내부에서 편집중 미리보기 화면.
     *
     * @param formId
     * @param model
     * @return String
     */
    @GetMapping("/{formId}/preview")
    fun getFormEditPreview(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        return formEditPreviewPage
    }

    /**
     * 문서양식 리스트 목록에서 선택한 미리보기 화면
     *
     * 위의 미리보기와 통합 검토 필요 (2021.06.03 Jung Hee Chan)
     *
     * @param formId
     * @param model
     * @return String
     */
    @GetMapping("/{formId}/view")
    fun getFormDesignerView(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        model.addAttribute("isView", true)
        return formDesignerEditPage
    }
}
