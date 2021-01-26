/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.cmdb.ciType.service.CITypeService
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
        private val ciTypeService: CITypeService
) {

    private val ciEditPage: String = "cmdb/ci/ciEdit"
    private val ciListModal: String = "cmdb/ci/ciListModal"

    /**
     * CI 신규 등록 화면 호출.
     */
    @GetMapping("/new")
    fun getCINew(): String {
        return ciEditPage
    }

    /**
     * CI 조회 화면 호출.
     */
    @GetMapping("")
    fun getCIsModal(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["tagSearch"] = request.getParameter("tagSearch")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result)
        model.addAttribute("ciListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return ciListModal;
    }
}
