/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.controller

import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.provider.dto.CITypeDto
import co.brainz.cmdb.provider.dto.CITypeListDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
@RequestMapping("/rest/cmdb/eg/types")
class CITypeController(
    private val ciTypeService: CITypeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Type 목록 조회
     */
    @GetMapping("")
    fun getCITypes(@RequestParam parameters: LinkedHashMap<String, Any>): List<CITypeListDto> {
        return ciTypeService.getCITypes(parameters["search"].toString())
    }

    /**
     * CMDB Type 단일 조회
     */
    @GetMapping("/{typeId}")
    fun getCIType(@PathVariable typeId: String): CITypeDto {
        return ciTypeService.getCIType(typeId)
    }

    /**
     * CMDB Type 등록
     */
    @PostMapping("")
    fun createCIType(@RequestBody jsonData: Any): Boolean {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return ciTypeService.createCIType(mapper.convertValue(jsonData, CITypeDto::class.java))
    }

    /**
     * CMDB Type 수정
     */
    @PutMapping("/{typeId}")
    fun updateCIType(
        @RequestBody ciTypeDto: CITypeDto,
        @PathVariable typeId: String
    ): Boolean {
        return ciTypeService.updateCIType(ciTypeDto, typeId)
    }

    /**
     * CMDB Type 삭제
     */
    @DeleteMapping("/{typeId}")
    fun deleteCIType(@PathVariable typeId: String): Boolean {
        return ciTypeService.deleteCIType(typeId)
    }
}
