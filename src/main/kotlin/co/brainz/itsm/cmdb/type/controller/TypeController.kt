/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.type.controller

import co.brainz.itsm.cmdb.type.service.TypeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/cmdb/types")
class TypeController(private val typeService: TypeService) {

    private val typeEditPage: String = "cmdb/typeEdit"

    /**
     * CMDB Type 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getCodeList(request: HttpServletRequest, model: Model): String {
        return typeEditPage
    }
}



