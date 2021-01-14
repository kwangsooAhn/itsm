/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.itsm.cmdb.ci.service.CIService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/cis")
class CIRestController(private val ciService: CIService) {
}
