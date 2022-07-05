/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.controller

import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import co.brainz.itsm.cmdb.ciAttribute.service.CIAttributeService
import org.slf4j.Logger
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
@RequestMapping("/rest/cmdb/attributes")
class CIAttributeRestController(private val ciAttributeService: CIAttributeService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Attribute 목록 조회.
     */
    @GetMapping("")
    fun getCIAttributes(
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciAttributeService.getCIAttributes(ciAttributeSearchCondition))
    }

    /**
     * CI Attribute 등록.
     */
    @PostMapping("")
    fun createCIAttribute(@RequestBody ciAttributeDto: CIAttributeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciAttributeService.saveCIAttribute(ciAttributeDto))
    }

    /**
     * CI Attribute 수정.
     */
    @PutMapping("/{attributeId}")
    fun updateCIAttribute(
        @PathVariable attributeId: String,
        @RequestBody ciAttributeDto: CIAttributeDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            ciAttributeService.updateCIAttribute(attributeId, ciAttributeDto)
        )
    }

    /**
     * CI Attribute 삭제.
     */
    @DeleteMapping("/{attributeId}")
    fun deleteAttribute(@PathVariable attributeId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciAttributeService.deleteCIAttribute(attributeId))
    }
}
