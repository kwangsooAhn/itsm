/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.controller

import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/attributes")
class CIAttributeController(private val ciAttributeService: CIAttributeService) {

    private val attributeSearchPage: String = "cmdb/attribute/attributeSearch"
    private val attributeListPage: String = "cmdb/attribute/attributeList"
    private val attributeEditPage: String = "cmdb/attribute/attributeEdit"
    private val attributeViewPage: String = "cmdb/attribute/attributeView"

    /**
     * CI Attribute 관리 검색 화면 호출.
     */
    @GetMapping("/search")
    fun getCIAttributeSearch(): String {
        return attributeSearchPage
    }

    /**
     * CI Attribute 관리 화면 호출.
     */
    @GetMapping("")
    fun getCIAttributes(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciAttributeService.getCIAttributes(params)
        model.addAttribute("attributeList", result)
        model.addAttribute("attributeListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return attributeListPage
    }

    /**
     * CI Attribute 등록 화면 호출.
     */
    @GetMapping("/new")
    fun getCIAttributeNew(): String {
        return attributeEditPage
    }

    /**
     * CI Attribute 수정 화면 호출.
     */
    @GetMapping("/{attributeId}/edit")
    fun getCIAttributeEdit(@PathVariable attributeId: String, model: Model): String {
        model.addAttribute("attribute", ciAttributeService.getCIAttribute(attributeId))
        return attributeEditPage
    }

    /**
     * CI Attribute 보기 화면 호출.
     */
    @GetMapping("/{attributeId}/view")
    fun getCIAttributeView(@PathVariable attributeId: String, model: Model): String {
        model.addAttribute("attribute", ciAttributeService.getCIAttribute(attributeId))
        return attributeViewPage
    }
}
