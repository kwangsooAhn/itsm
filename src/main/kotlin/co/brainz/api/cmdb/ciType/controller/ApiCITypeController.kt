/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciType.controller

import co.brainz.api.ApiUtil
import co.brainz.api.cmdb.ciType.service.ApiCITypeService
import co.brainz.api.dto.SearchDto
import co.brainz.cmdb.dto.CITypeDto
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
@RequestMapping("/api/cmdb/types")
class ApiCITypeController(
    private val apiCITypeService: ApiCITypeService
) : ApiUtil() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Type 목록 조회
     */
    @GetMapping("")
    fun getCITypes(
        request: HttpServletRequest,
        searchDto: SearchDto
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.getCITypes(super.setSearchParam(searchDto)))
    }

    /**
     * CI Type 단일 목록 조회
     */
    @GetMapping("/{typeId}")
    fun getCIType(
        request: HttpServletRequest,
        @PathVariable typeId: String
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.getCIType(typeId))
    }

    /**
     * CI Type 상세 조회
     */
    @GetMapping("/{typeId}/detail")
    fun getCITypeDetail(
        request: HttpServletRequest,
        @PathVariable typeId: String
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.getCITypeDetail(typeId))
    }

    /**
     * CI Type 등록
     */
    @PostMapping("")
    fun createCIType(
        request: HttpServletRequest,
        @RequestBody ciTypeDto: CITypeDto
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.createCIType(ciTypeDto))
    }

    /**
     * CI Type 수정
     */
    @PutMapping("/{typeId}")
    fun updateCIType(
        request: HttpServletRequest,
        @PathVariable typeId: String,
        @RequestBody ciTypeDto: CITypeDto
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.updateCIType(typeId, ciTypeDto))
    }

    /**
     * CI Type 삭제
     */
    @DeleteMapping("/{typeId}")
    fun deleteCIType(
        request: HttpServletRequest,
        @PathVariable typeId: String
    ): ResponseEntity<*> {
        return super.responseValue(request, apiCITypeService.deleteCIType(typeId))
    }
}
