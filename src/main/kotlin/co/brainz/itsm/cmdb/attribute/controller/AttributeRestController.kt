/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.attribute.controller

import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.itsm.cmdb.attribute.service.AttributeService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/attributes")
class AttributeRestController(private val attributeService: AttributeService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Attribute 리스트 조회 (스크롤).
     */
    @GetMapping("")
    fun getAttributes(request: HttpServletRequest, model: Model): List<CmdbAttributeListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        return attributeService.getAttributes(params)
    }
}
