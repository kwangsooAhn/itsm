/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.controller

import co.brainz.framework.organization.dto.OrganizationRoleDto
import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.service.OrganizationService
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
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
@RequestMapping("/rest/organizations")
class OrganizationRestController(
    private val organizationService: OrganizationService
) {

    /**
     * 조직 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getOrganizations(
        organizationSearchCondition: OrganizationSearchCondition
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            organizationService.getOrganizationList(organizationSearchCondition)
        )
    }

    /**
     * 조직 상세 정보 조회
     */
    @GetMapping("/{organizationId}")
    fun getDetailOrganization(
        @PathVariable organizationId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(organizationService.getDetailOrganization(organizationId))
    }

    /**
     * 조직 등록
     */
    @PostMapping("/", "")
    fun createOrganization(
        @RequestBody organizationRoleDto: OrganizationRoleDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(organizationService.createOrganization(organizationRoleDto))
    }

    /**
     * 조직 정보 수정
     */
    @PutMapping("/{organizationId}")
    fun updateOrganization(
        @RequestBody organizationRoleDto: OrganizationRoleDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(organizationService.updateOrganization(organizationRoleDto))
    }

    /**
     * 조직 정보 삭제
     */
    @DeleteMapping("/{organizationId}")
    fun deleteOrganization(@PathVariable organizationId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(organizationService.deleteOrganization(organizationId))
    }
}
