/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.constants.ItsmConstants
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/cmdb/cis")
class CIController(
    private val ciService: CIService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val ciSearchPage: String = "cmdb/ci/ciSearch"
    private val ciListPage: String = "cmdb/ci/ciList"
    private val ciListFragment: String = "cmdb/ci/ciList :: list"
    private val ciViewPage: String = "cmdb/ci/ciView"
    private val ciEditModal: String = "cmdb/ci/ciEditModal"
    private val ciViewModal: String = "cmdb/ci/ciViewModal"
    private val ciListModal: String = "cmdb/ci/ciListModal"

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
    fun getCIList(
        request: HttpServletRequest,
        @RequestParam(value = "isScroll", required = false) isScroll: Boolean,
        model: Model
    ): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        params["tags"] = request.getParameter("tagSearch")
        params["flag"] = request.getParameter("flag")
        params["offset"] = request.getParameter("offset") ?: "0"
        params["limit"] = ItsmConstants.SEARCH_DATA_COUNT
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result.data)
        model.addAttribute("ciListCount", result.totalCount)
        return if (isScroll) ciListFragment else ciListPage
    }

    /**
     * CI ITSM 보기 화면 호출.
     */
    @GetMapping("/{ciId}/view")
    fun getCIView(request: HttpServletRequest, model: Model, @PathVariable ciId: String): String {
        val ciData = ciService.getCI(ciId)
        val ciHistoryList = ciService.getCIHistory(ciId)
        val ciRelationList = ciService.getCIRelation(ciId)
        model.addAttribute("ciData", ciData)
        model.addAttribute("ciHistoryList", ciHistoryList)
        model.addAttribute("userInfo", currentSessionUser.getUserDto())
        model.addAttribute("ciRelationList", ciRelationList)
        return ciViewPage
    }

    /**
     * CI 신규 등록 화면 호출.
     */
    @GetMapping("/component/new")
    fun getCIComponentNew(): String {
        return ciEditModal
    }

    /**
     * CI 수정 화면 호출.
     * 화면에서 사용자가 수정한 데이터를 모달에 함께 출력한다.
     */
    @PostMapping("/component/edit")
    fun getCIComponentEdit(request: HttpServletRequest, @RequestBody modifyCIData: String, model: Model): String {
        // TODO: 세션 정보를 화면에서 처리하도록 수정
        model.addAttribute(
            "ciData", ciService.getCIData(
                request.getParameter("ciId"),
                request.getParameter("componentId"),
                request.getParameter("instanceId"),
                modifyCIData
            )
        )
        model.addAttribute("userInfo", currentSessionUser.getUserDto())
        return ciEditModal
    }

    /**
     * CI Component 보기 화면 호출.
     */
    @GetMapping("/component/view")
    fun getCIComponentView(request: HttpServletRequest, model: Model): String {
        // TODO: 세션 정보를 화면에서 처리하도록 수정
        model.addAttribute(
            "ciData", ciService.getCI(
                request.getParameter("ciId")
            )
        )
        model.addAttribute("userInfo", currentSessionUser.getUserDto())
        return ciViewModal
    }

    /**
     * CI 컴포넌트 - CI 조회 화면 호출.
     */
    @GetMapping("/component/list")
    fun getCIComponentList(request: HttpServletRequest, model: Model): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        params["tags"] = request.getParameter("tagSearch")
        params["flag"] = request.getParameter("flag")
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result.data)
        model.addAttribute("ciListCount", result.totalCount)
        return ciListModal
    }
}
