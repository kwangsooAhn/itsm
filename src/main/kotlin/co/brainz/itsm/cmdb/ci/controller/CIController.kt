/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cmdb/cis")
class CIController(private val ciService: CIService) {
}
