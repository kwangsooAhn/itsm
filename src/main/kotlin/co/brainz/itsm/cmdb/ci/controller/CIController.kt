/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.cmdb.ciType.constants.CITypeConstants
import co.brainz.itsm.code.service.CodeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/cis")
class CIController(
    private val ciService: CIService,
    private val codeService: CodeService)
{

    private val ciSearchPage: String = "cmdb/ci/ciSearch"
    private val ciListPage: String = "cmdb/ci/ciList"

    /**
     * CI 조회 검색 화면 호출
     */
    @GetMapping("/search")
    fun getCmdbCiSearch(model: Model): String {
        model.addAttribute("ciTypeList", codeService.selectCodeByParent(CITypeConstants.CMDB_TYPE_P_CODE))
        return ciSearchPage
    }

    /**
     * CI 조회 목록 화면 호출
     */
    @GetMapping("")
    fun getCmdbCiList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["tagSearch"] = request.getParameter("tagSearch")
        params["searchGroupName"] = request.getParameter("searchGroupName")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciService.getCis(params)
        model.addAttribute("ciList", result)
        model.addAttribute("ciListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return ciListPage
    }
}
