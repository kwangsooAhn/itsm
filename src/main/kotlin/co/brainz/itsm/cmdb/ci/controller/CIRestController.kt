/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.itsm.cmdb.ci.service.CIService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/cis")
class CIRestController(private val ciService: CIService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB CI 조회 목록 리스트 조회.
     */
    @GetMapping("")
    fun getCIs(request: HttpServletRequest, model: Model): List<CIListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["tagSearch"] = request.getParameter("tagSearch")
        params["offset"] = request.getParameter("offset") ?: "0"
        return ciService.getCIs(params)
    }
}
