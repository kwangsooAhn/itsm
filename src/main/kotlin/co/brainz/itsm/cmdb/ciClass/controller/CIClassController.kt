/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/class")
class CIClassController(private val ciClassService: CIClassService) {

    private val classEditPage: String = "cmdb/classEdit"
    private val classListPage: String = "cmdb/classList"

    /**
     * CI Class 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCmdbEdit(request: HttpServletRequest, model: Model): String {
        return classEditPage
    }

    /**
     * CI Class 리스트 호출 처리. (임시)
     */
    @GetMapping("")
    fun getCmdbClassList(request: HttpServletRequest, model: Model): String {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        val result = ciClassService.getCmdbClasses(params)
        model.addAttribute("classList", result)
        return classListPage
    }
}
