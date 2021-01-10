/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.controller

import co.brainz.cmdb.attribute.service.AttributeService
import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
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
class AttributeController(
    private val attributeService: AttributeService,
    private val cmdbDummyProvider: CmdbDummyProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 프로세스 목록 조회.
     */
    @GetMapping("")
    fun getCmdbAttributes(@RequestParam parameters: LinkedHashMap<String, Any>): List<CmdbAttributeListDto> {
        return attributeService.getCmdbAttributes(parameters)
    }


    @PostMapping("")
    fun createCmdbAttribute(@RequestBody cmdbAttributeDto: CmdbAttributeDto): Boolean {
        return true
    }

    @GetMapping("/{attributeId}")
    fun getCmdbAttribute(@PathVariable attributeId: String): CmdbAttributeDto {
        return cmdbDummyProvider.getDummyAttribute(attributeId)
    }

    @PutMapping("/{attributeId}")
    fun updateCmdbAttribute(@RequestBody cmdbAttributeDto: CmdbAttributeDto): Boolean {
        return true
    }

    @DeleteMapping("/{attributeId}")
    fun deleteCmdbAttribute(@PathVariable attributeId: String): Boolean {
        return true
    }
}
