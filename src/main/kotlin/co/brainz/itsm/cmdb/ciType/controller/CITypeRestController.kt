/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.controller

import co.brainz.cmdb.dto.CITypeDto
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.cmdb.ciType.service.CITypeService
import javax.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
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
@RequestMapping("/rest/cmdb/types")
class CITypeRestController(private val ciTypeService: CITypeService) {

    /**
     * CI Type 목록 조회
     */
    @GetMapping("/", "")
    fun getCITypesTree(
        request: HttpServletRequest,
        @RequestParam(value = "search", required = false, defaultValue = "") search: String,
        model: Model
    ): ResponseEntity<ZResponse> {
        val params = LinkedHashMap<String, Any>()
        params["search"] = search
        return ZAliceResponse.response(ciTypeService.getCITypesTree(params))
    }

    /**
     * CI Type 단일 조회
     */
    @GetMapping("/{typeId}")
    fun getCIType(@PathVariable typeId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciTypeService.getCIType(typeId))
    }

    /**
     * CI Type 등록
     */
    @PostMapping("")
    fun createCITypes(@RequestBody ciTypeDto: CITypeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciTypeService.createCIType(ciTypeDto))
    }

    /**
     * CI Type 수정
     */
    @PutMapping("/{typeId}")
    fun updateCIType(
        @RequestBody ciTypeDto: CITypeDto,
        @PathVariable typeId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciTypeService.updateCIType(ciTypeDto, typeId))
    }

    /**
     * CI Type 삭제
     */
    @DeleteMapping("/{typeId}")
    fun deleteCIType(@PathVariable typeId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(ciTypeService.deleteCIType(typeId))
    }
}
