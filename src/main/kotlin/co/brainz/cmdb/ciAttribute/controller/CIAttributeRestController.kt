/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.controller

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/eg/attributes")
class CIAttributeRestController(
    private val ciAttributeService: CIAttributeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Attribute 목록 조회.
     */
    @GetMapping("")
    fun getCIAttributes(@RequestParam parameters: LinkedHashMap<String, Any>): List<CIAttributeListDto> {
        return ciAttributeService.getCIAttributes(parameters)
    }

    /**
     * CI Attribute 신규 등록.
     */
    @PostMapping("")
    fun createCIAttribute(@RequestBody ciAttributeDto: CIAttributeDto): RestTemplateReturnDto {
        return ciAttributeService.createCIAttribute(ciAttributeDto)
    }

    /**
     * CI Attribute 단일 조회.
     */
    @GetMapping("/{attributeId}")
    fun getCIAttribute(@PathVariable attributeId: String): CIAttributeDto {
        return ciAttributeService.getCIAttribute(attributeId)
    }

    /**
     * CI Attribute 수정.
     */
    @PutMapping("/{attributeId}")
    fun updateCIAttribute(@RequestBody ciAttributeDto: CIAttributeDto): RestTemplateReturnDto {
        return ciAttributeService.updateCIAttribute(ciAttributeDto)
    }

    /**
     * CI Attribute 삭제.
     */
    @Transactional
    @DeleteMapping("/{attributeId}")
    fun deleteCIAttribute(@PathVariable attributeId: String): Boolean {
        return ciAttributeService.deleteCIAttribute(attributeId)
    }
}
