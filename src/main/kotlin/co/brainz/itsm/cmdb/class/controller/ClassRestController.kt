/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.`class`.controller

import co.brainz.itsm.cmdb.`class`.service.ClassService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/classes")
class ClassRestController(private val classService: ClassService) {
}
