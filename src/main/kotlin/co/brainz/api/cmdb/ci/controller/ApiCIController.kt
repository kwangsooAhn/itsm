/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ci.controller

import co.brainz.api.ApiUtil
import co.brainz.api.cmdb.ci.service.ApiCIService
import co.brainz.cmdb.dto.CIDto
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
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
@RequestMapping("/api/cmdb/cis")
class ApiCIController(
    private val apiCIService: ApiCIService
) : ApiUtil() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI 목록 조회
     */
    @GetMapping("")
    fun getCIs(
        request: HttpServletRequest
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(apiCIService.getCIs(super.setRequestToParam(request)))
    }

    /**
     * CI 단일 목록 조회
     */
    @GetMapping("/{ciId}")
    fun getCI(
        request: HttpServletRequest,
        @PathVariable ciId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(apiCIService.getCI(ciId))
    }

    /**
     * CI 상세 조회
     */
    @GetMapping("/{ciId}/detail")
    fun getCIDetail(
        request: HttpServletRequest,
        @PathVariable ciId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(apiCIService.getCIDetail(ciId))
    }

    /**
     * CI 등록
     */
    @PostMapping("")
    fun createCI(
        request: HttpServletRequest,
        @RequestBody ciDto: CIDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(apiCIService.createCI(ciDto))
    }

    /**
     * CI 수정
     */
    @PutMapping("/{ciId}")
    fun updateCI(
        request: HttpServletRequest,
        @PathVariable ciId: String,
        @RequestBody ciDto: CIDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(apiCIService.updateCI(ciId, ciDto))
    }

    /**
     * CI 삭제
     */
    @DeleteMapping("/{ciId}")
    fun deleteCI(
        request: HttpServletRequest,
        @PathVariable ciId: String,
        @RequestBody ciDto: CIDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            apiCIService.deleteCI(ciId, ciDto)
        )
    }
}
