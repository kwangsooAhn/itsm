/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.controller

import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/types")
class CITypeController() {

    private val typeEditPage: String = "cmdb/typeEdit"

    /**
     * CMDB Type 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCmdbTypeList(request: HttpServletRequest, model: Model): String {
        return typeEditPage
    }
}
