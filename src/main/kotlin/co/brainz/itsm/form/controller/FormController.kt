package co.brainz.itsm.form.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.form.constants.FormConstants
import co.brainz.workflow.form.repository.FormRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

/**
 * ### 폼(문서양식) 관련 화면 호출 처리용 클래스.
 *
 * FormController에서 처리하는 모든 호출은 View 혹은 View + Data를 반환한다.
 * 즉, View가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FormRestController에서 담당한다.
 *
 * @author Woo Dajung
 * @see co.brainz.itsm.form.controller.FormRestController
 */
@Controller
@RequestMapping("/forms")
class FormController(private val codeService: CodeService,
                     private val formRepository: FormRepository) {

    private val formSearchPage: String = "form/formSearch"
    private val formListPage: String = "form/formList"
    private val formEditPage: String = "form/formEdit"
    private val formDesignerEditPage: String = "form/formDesignerEdit"

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
        model.addAttribute("formList", WFEngine().form(formRepository).formList(request.getParameter("search")))
        return formListPage
    }

    /**
     * 폼 기본 정보 등록 화면.
     */
    @GetMapping("/new")
    fun getFormNew(request: HttpServletRequest, model: Model): String {
        //언어 목록
        model.addAttribute("langList", codeService.selectCodeByParent(FormConstants.FormLang.P_CODE.value))
        //TODO 템플릿 정보 가져오기

        return formEditPage
    }

    /**
     * 폼 디자이너 편집 화면.
     */
    @GetMapping("/{formId}/edit")
    fun getFormDesignerEdit(@PathVariable formId: String, model: Model): String {
        //TODO 컴포넌트 상세 정보 가져오기
        model.addAttribute("form", WFEngine().form(formRepository).form(formId))
        return formDesignerEditPage
    }

    /**
     * 폼 삭제.
     */
    @Transactional
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): String {
        WFEngine().form(formRepository).deleteForm(formId)
        return formListPage
    }
}
