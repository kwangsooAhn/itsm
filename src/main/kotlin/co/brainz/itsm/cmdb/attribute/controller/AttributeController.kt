/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.attribute.controller

import co.brainz.itsm.cmdb.attribute.service.AttributeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/attributes")
class AttributeController(private val attributeService: AttributeService) {

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
        val result = attributeService.getAttributes(params)
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
