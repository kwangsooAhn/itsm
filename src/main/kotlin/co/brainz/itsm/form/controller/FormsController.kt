/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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
class FormsController(private val formService: FormService) {

    private val formSearchPage: String = "formDesigner/formSearch"
    private val formListPage: String = "formDesigner/formList"
    private val formListFragment: String = "formDesigner/formList :: list"

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
    @GetMapping("")
    fun getFormList(
        request: HttpServletRequest,
        @RequestParam(value = "isScroll", required = false) isScroll: Boolean,
        model: Model
    ): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search") ?: ""
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = formService.findForms(params)
        model.addAttribute("formList", result)
        model.addAttribute("formListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return if (isScroll) formListFragment else formListPage
    }
}
