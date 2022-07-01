/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/icons")
class CIIconController {
    private val iconListPage: String = "cmdb/icon/iconList"

    @GetMapping("")
    fun getFiles(model: Model): String {
        return iconListPage
    }
}
