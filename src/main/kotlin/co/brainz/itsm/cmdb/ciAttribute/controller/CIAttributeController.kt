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
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/attributes")
class CIAttributeController(private val ciAttributeService: CIAttributeService) {

    private val attributeSearchPage: String = "cmdb/attributeSearch"
    private val attributeListPage: String = "cmdb/attributeList"
    private val attributeEditPage: String = "cmdb/attributeEdit"
    private val attributeViewPage: String = "cmdb/attributeView"

    @GetMapping("/search")
    fun getAttributeSearch(): String {
        return attributeSearchPage
    }

    @GetMapping("")
    fun getAttributes(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciAttributeService.getAttributes(params)
        model.addAttribute("attributeList", result)
        model.addAttribute("attributeListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return attributeListPage
    }

    fun getAttributeNew(): String {
        return attributeEditPage
    }

    fun getAttributeEdit(): String {
        return attributeEditPage
    }

    fun getAttributeView(): String {
        return attributeViewPage
    }
}
