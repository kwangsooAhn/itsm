/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/class")
class CIClassController {

    private val classEditPage: String = "cmdb/classEdit"

    /**
     * CI Class 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCmdbEdit(request: HttpServletRequest, model: Model): String {
        return classEditPage
    }
}
