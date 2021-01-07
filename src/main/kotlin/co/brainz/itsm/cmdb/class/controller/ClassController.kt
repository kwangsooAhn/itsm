/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.`class`.controller

import co.brainz.itsm.cmdb.`class`.service.ClassService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/classes")
class ClassController(private val classService: ClassService) {

    @GetMapping("")
    fun getClassSearch() {

    }
}
