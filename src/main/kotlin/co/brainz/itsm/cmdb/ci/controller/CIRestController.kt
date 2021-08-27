/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.controller

import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIListReturnDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.itsm.cmdb.ci.dto.CIComponentDataDto
import co.brainz.itsm.cmdb.ci.service.CIService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/cis")
class CIRestController(private val ciService: CIService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("")
    fun getCIs(): CIListReturnDto {
        return ciService.getCIs(null)
    }

    @GetMapping("/{ciId}")
    fun getCI(request: HttpServletRequest, model: Model, @PathVariable ciId: String): CIDetailDto {
        return ciService.getCI(ciId)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 등록
     */
    @PostMapping("/{ciId}/data")
    fun saveCIComponentData(@PathVariable ciId: String, @RequestBody ciComponentData: String): Boolean {
        return ciService.saveCIComponentData(ciId, ciComponentData)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 삭제
     */
    @DeleteMapping("/data")
    fun deleteCIComponentData(request: HttpServletRequest): Boolean {
        return ciService.deleteCIComponentData(
            request.getParameter("ciId"),
            request.getParameter("componentId")
        )
    }

    /**
     * CI 컴포넌트 - CI 컴포넌트 세부 정보 조회
     */
    @GetMapping("/{ciId}/data")
    fun getCIComponentData(request: HttpServletRequest, @PathVariable ciId: String): CIComponentDataDto? {
        return ciService.getCIComponentData(
            ciId,
            request.getParameter("componentId"),
            request.getParameter("instanceId")
        )
    }

    /**
     * CI 연관 관계 데이터 조회
     */
    @GetMapping("/{ciId}/relation")
    fun getCIRelations(@PathVariable ciId: String): List<CIRelationDto> {
        return ciService.getCIRelation(ciId)
    }
}
