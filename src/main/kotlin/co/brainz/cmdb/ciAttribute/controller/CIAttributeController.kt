/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.controller

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
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
class CIAttributeController(
    private val ciAttributeService: CIAttributeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Attribute 목록 조회.
     */
    @GetMapping("")
    fun getCmdbAttributes(@RequestParam parameters: LinkedHashMap<String, Any>): List<CmdbAttributeListDto> {
        return ciAttributeService.getCmdbAttributes(parameters)
    }

    /**
     * CMDB Attribute 신규 등록.
     */
    @PostMapping("")
    fun createCmdbAttribute(@RequestBody cmdbAttributeDto: CmdbAttributeDto): RestTemplateReturnDto {
        return ciAttributeService.createCmdbAttribute(cmdbAttributeDto)
    }

    /**
     * CMDB Attribute 단일 조회.
     */
    @GetMapping("/{attributeId}")
    fun getCmdbAttribute(@PathVariable attributeId: String): CmdbAttributeDto {
        return ciAttributeService.getCmdbAttribute(attributeId)
    }

    /**
     * CMDB Attribute 수정.
     */
    @PutMapping("/{attributeId}")
    fun updateCmdbAttribute(@RequestBody cmdbAttributeDto: CmdbAttributeDto): RestTemplateReturnDto {
        return ciAttributeService.updateCmdbAttribute(cmdbAttributeDto)
    }

    /**
     * CMDB Attribute 삭제.
     */
    @Transactional
    @DeleteMapping("/{attributeId}")
    fun deleteCmdbAttribute(@PathVariable attributeId: String): Boolean {
        return ciAttributeService.deleteCmdbAttribute(attributeId)
    }
}
