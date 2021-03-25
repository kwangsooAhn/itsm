/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciAttribute.controller

import co.brainz.api.ApiUtil
import co.brainz.api.cmdb.ciAttribute.service.ApiCIAttributeService
import co.brainz.api.dto.SearchDto
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cmdb/attributes")
class ApiCIAttributeController(
    private val apiCIAttributeService: ApiCIAttributeService
) : ApiUtil() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Attribute 목록 조회
     */
    @GetMapping("")
    fun getAttributes(
        request: HttpServletRequest,
        searchDto: SearchDto
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.getCIAttributes(super.setSearchParam(searchDto)))
    }

    /**
     * CI Attribute 단일 목록 조회
     */
    @GetMapping("/{attributeId}")
    fun getAttribute(
        request: HttpServletRequest,
        @PathVariable attributeId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.getCIAttribute(attributeId))
    }

    /**
     * CI Attribute 상세 조회
     */
    @GetMapping("/{attributeId}/detail")
    fun getAttributeDetail(
        request: HttpServletRequest,
        @PathVariable attributeId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.getCIAttributeDetail(attributeId))
    }

    /**
     * CI Attribute 등록
     */
    @PostMapping("")
    fun createAttribute(
        request: HttpServletRequest,
        @RequestBody attributeData: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.createCIAttribute(attributeData))
    }

    /**
     * CI Attribute 수정
     */
    @PutMapping("/{attributeId}")
    fun updateAttribute(
        request: HttpServletRequest,
        @PathVariable attributeId: String,
        @RequestBody attributeData: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.updateCIAttribute(attributeId, attributeData))
    }

    /**
     * CI Attribute 삭제
     */
    @DeleteMapping("/{attributeId}")
    fun deleteAttribute(
        request: HttpServletRequest,
        @PathVariable attributeId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIAttributeService.deleteCIAttribute(attributeId))
    }
}
