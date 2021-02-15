/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/cis")
class CIController(private val ciService: CIService) {

    private val ciSearchPage: String = "cmdb/ci/ciSearch"
    private val ciListPage: String = "cmdb/ci/ciList"
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
    @GetMapping("/component-new")
    fun getCINew(): String {
        return ciEditModal
    }

    /**
     * CI 수정 화면 호출.
     * 화면에서 사용자가 수정한 데이터를 모달에 함께 출력한다.
     */
    @PostMapping("/component-edit")
    fun getCIEdit(request: HttpServletRequest, @RequestBody modifyCIData: String, model: Model): String {
        model.addAttribute(
            "ciData", ciService.getCIData(
                request.getParameter("ciId"),
                request.getParameter("componentId"),
                request.getParameter("instanceId"),
                modifyCIData
            )
        )
        return ciEditModal
    }

    /**
     * CI Component 보기 화면 호출.
     */
    @GetMapping("/component-view")
    fun getCICompoentView(request: HttpServletRequest, model: Model): String {
        val ciData = ciService.getCI(request.getParameter("ciId"))
        val tags = JsonArray()
        if (ciData.ciTags != null) {
            ciData.ciTags!!.forEach {
                val tagData = JsonObject()
                tagData.addProperty("id", it.tagId.toString())
                tagData.addProperty("value", it.tagName)
                tags.add(tagData)
            }
        }
        model.addAttribute("ciData", ciData)
        model.addAttribute("tags", tags)
        return ciViewModal
    }

    /**
     * CI ITSM 보기 화면 호출.
     */
    @GetMapping("/{ciId}/view")
    fun getCIView(request: HttpServletRequest, model: Model, @PathVariable ciId: String): String {
        val ciData = ciService.getCI(ciId)
        val tags = JsonArray()
        if (ciData.ciTags != null) {
            ciData.ciTags!!.forEach {
                val tagData = JsonObject()
                tagData.addProperty("id", it.tagId.toString())
                tagData.addProperty("value", it.tagName)
                tags.add(tagData)
            }
        }
        model.addAttribute("ciData", ciData)
        model.addAttribute("tags", tags)
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
        val result = ciService.getCIs(params)
        model.addAttribute("ciList", result)
        model.addAttribute("ciListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return ciListModal
    }
}
