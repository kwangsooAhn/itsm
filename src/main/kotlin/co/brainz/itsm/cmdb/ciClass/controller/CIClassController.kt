/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
    fun getCIClassList(request: HttpServletRequest, model: Model): String {
        return classEditPage
    }

    /**
     * CI Class 관리 화면 Attribute 리스트 모달
     */
    @GetMapping("/view-pop/attributes")
    fun getCIClassAttributeList(request: HttpServletRequest, model: Model): String {
        val params = LinkedHashMap<String, Any>()
        params["search"] = request.getParameter("search")
        val classId = request.getParameter("classId")
        val attributeList = ciAttributeService.getCIAttributes(params)
        var addAttributeList: List<CIClassToAttributeDto>? = null
        var extendsAttributeList: List<CIClassToAttributeDto>? = null

        if (classId != "") {
            addAttributeList = ciClassService.getCIClass(classId).attributes
            extendsAttributeList = ciClassService.getCIClass(classId).extendsAttributes
        }

        val classAttributeList = ciClassService.getClassAttributeList(
            attributeList.data,
            addAttributeList,
            extendsAttributeList
        )

        model.addAttribute("attributeList", classAttributeList)
        return classAttributeListPage
    }
}
