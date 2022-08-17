/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.serviceCategory.dto.ServiceCategoryDto
import co.brainz.itsm.serviceCategory.service.ServiceCategory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/service-category")
class ServiceCategoryRestController(
    private val serviceCategory: ServiceCategory
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 서비스 카테고리 전체 조회
     */
    @GetMapping("/", "")
    fun getServiceList(
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(serviceCategory.getServiceList(searchValue))
    }

    /**
     * 서비스 카테고리 상세 조회
     */
    @GetMapping("/{serviceCode}")
    fun getServiceDetail(
        @PathVariable serviceCode: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(serviceCategory.getServiceDetail(serviceCode))
    }

    /**
     * 서비스 카테고리 신규 등록
     */
    @PostMapping("/", "")
    fun createService(
        @RequestBody serviceCategoryDto: ServiceCategoryDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(serviceCategory.createService(serviceCategoryDto))
    }

    /**
     * 서비스 카테고리 수정
     */
    @PutMapping("/{serviceCode}")
    fun updateService(
        @PathVariable serviceCode: String,
        @RequestBody serviceCategoryDto: ServiceCategoryDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(serviceCategory.updateService(serviceCode, serviceCategoryDto))
    }
}
