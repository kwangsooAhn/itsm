/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/cis")
class CIController(private val ciService: CIService) {

    private val ciSearchPage: String = "cmdb/ci/ciSearch"
    private val ciListPage: String = "cmdb/ci/ciList"
    private val ciEditModal: String = "cmdb/ci/ciEditModal"
    private val ciListModal: String = "cmdb/ci/ciListModal"
    private val ciViewModal: String = "cmdb/ci/ciViewModal"

    /**
     * CI 조회 검색 화면 호출
     */
    @GetMapping("/search")
    fun getCISearch(model: Model): String {
        return ciSearchPage
    }

    /**
     * CI 조회 목록 화면 호출
     */
    @GetMapping("")
    fun getCIList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["tagSearch"] = request.getParameter("tagSearch")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result)
        model.addAttribute("ciListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return ciListPage
    }

    /**
     * CI 신규 등록 화면 호출.
     */
    @GetMapping("/new")
    fun getCINew(): String {
        return ciEditModal
    }

    /**
     * CI 상세 화면 호출.
     */
    @GetMapping("/view/{ciId}")
    fun getCI(request: HttpServletRequest, model: Model, @PathVariable ciId: String): String {
        val result = ciService.getCI(ciId)
        model.addAttribute("ci", result)
        return ciViewModal
    }

    /**
     * CI 컴포넌트 - CI 조회 화면 호출.
     */
    @GetMapping("/view-pop")
    fun getCIsModal(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["tagSearch"] = request.getParameter("tagSearch")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result)
        model.addAttribute("ciListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return ciListModal
    }
}
