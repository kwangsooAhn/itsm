/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.controller

import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/attributes")
class CIAttributeRestController(private val ciAttributeService: CIAttributeService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Attribute 리스트 조회 (스크롤).
     */
    @GetMapping("")
    fun getCIAttributes(request: HttpServletRequest, model: Model): List<CIAttributeListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        params["offset"] = request.getParameter("offset") ?: "0"
        return ciAttributeService.getCIAttributes(params)
    }

    /**
     * CI Attribute 등록.
     */
    @PostMapping("")
    fun createCIAttribute(@RequestBody attributeData: String): String {
        return ciAttributeService.saveCIAttribute(attributeData)
    }

    /**
     * CI Attribute 수정.
     */
    @PutMapping("/{attributeId}")
    fun updateCIAttribute(
        @PathVariable attributeId: String,
        @RequestBody attributeData: String
    ): String {
        return ciAttributeService.updateCIAttribute(attributeId, attributeData)
    }

    /**
     * CI Attribute 삭제.
     */
    @DeleteMapping("/{attributeId}")
    fun deleteAttribute(@PathVariable attributeId: String): String {
        return ciAttributeService.deleteCIAttribute(attributeId)
    }
}
