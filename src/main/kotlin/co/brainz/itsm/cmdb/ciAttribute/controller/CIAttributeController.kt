/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.controller

import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
    private val attributeListModal: String = "cmdb/attribute/attributeListModal"

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
    fun getCIAttributes(ciAttributeSearchCondition: CIAttributeSearchCondition, model: Model): String {
        val result = ciAttributeService.getCIAttributes(ciAttributeSearchCondition)
        model.addAttribute("attributeList", result.data)
        model.addAttribute("paging", result.paging)
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

    /**
     * CI Attribute 리스트 모달 호출 (Group List 제외, 자기 자신 제외)
     */
    @GetMapping("/{attributeId}/list-modal")
    fun getCIAttributeListModal(@PathVariable attributeId: String, request: HttpServletRequest, model: Model): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        val attributeList = ciAttributeService.getCIAttributeListNotInGroupList(attributeId, CIAttributeSearchCondition(
            searchValue = params["search"].toString(),
            pageNum = 0L,
            contentNumPerPage = 0L
        ))
        model.addAttribute("attributeList", attributeList)
        return attributeListModal
    }
}
