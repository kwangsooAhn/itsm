/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.type.controller

import co.brainz.itsm.cmdb.type.service.TypeService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/types")
class TypeController(private val typeService: TypeService) {
}
