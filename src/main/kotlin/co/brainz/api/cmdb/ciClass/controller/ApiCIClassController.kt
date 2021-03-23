/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciClass.controller

import co.brainz.api.ApiUtil
import co.brainz.api.cmdb.ciClass.service.ApiCIClassService
import co.brainz.api.dto.SearchDto
import co.brainz.cmdb.provider.dto.CIClassDto
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
@RequestMapping("/api/cmdb/classes")
class ApiCIClassController(
    private val apiCIClassService: ApiCIClassService
) : ApiUtil() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Class 목록 조회
     */
    @GetMapping("")
    fun getCIClasses(
        request: HttpServletRequest,
        searchDto: SearchDto
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.getCIClasses(super.setSearchParam(searchDto)))
    }

    /**
     * CI Class 목록 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCIClass(
        request: HttpServletRequest,
        @PathVariable classId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.getCIClass(classId))
    }

    /**
     * CI Class 상세 조회
     */
    @GetMapping("/{classId}/detail")
    fun getCIClassDetail(
        request: HttpServletRequest,
        @PathVariable classId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.getCIClassDetail(classId))
    }

    /**
     * CI Class 에서 적용된 Attribute 목록 조회
     */
    @GetMapping("/{classId}/attributes")
    fun getCIClassAttributes(
        request: HttpServletRequest,
        @PathVariable classId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.getCIClassAttributes(classId))
    }

    /**
     * CI Class 등록
     */
    @PostMapping("")
    fun createCIClass(
        request: HttpServletRequest,
        @RequestBody ciClassDto: CIClassDto
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.createCIClass(ciClassDto))
    }

    /**
     * CI Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCIClass(
        request: HttpServletRequest,
        @PathVariable classId: String,
        @RequestBody ciClassDto: CIClassDto
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.updateCIClass(classId, ciClassDto))
    }

    /**
     * CI Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCIClass(
        request: HttpServletRequest,
        @PathVariable classId: String
    ): ResponseEntity<*> {
        return responseValue(request, apiCIClassService.deleteCIClass(classId))
    }
}
