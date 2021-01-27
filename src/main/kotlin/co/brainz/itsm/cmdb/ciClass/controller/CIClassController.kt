/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/class")
class CIClassController(
    private val ciAttributeService: CIAttributeService,
    private val ciClassService: CIClassService
) {

    private val classEditPage: String = "cmdb/class/classEdit"
    private val classAttributeListPage: String = "cmdb/class/classAttributeList"

    /**
     * CI Class 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCmdbClassList(request: HttpServletRequest, model: Model): String {
        return classEditPage
    }

    /**
     * CI Class 관리 화면 Attribute 리스트 모달
     */
    @GetMapping("/view-pop/attributes")
    fun getCmdbClassAttributeList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        val classId = request.getParameter("classId")
        val attributeList = ciAttributeService.getCIAttributes(params)
        var addAttributeList: List<CmdbClassToAttributeDto>? = null
        var extendsAttributeList: List<CmdbClassToAttributeDto>? = null

        if (classId != "") {
            addAttributeList = ciClassService.getCmdbClass(classId).attributes
            extendsAttributeList = ciClassService.getCmdbClass(classId).extendsAttributes
        }

        val classAttributeList = ciClassService.getClassAttributeList(
            attributeList,
            addAttributeList,
            extendsAttributeList
        )

        model.addAttribute("attributeList", classAttributeList)
        return classAttributeListPage
    }
}
