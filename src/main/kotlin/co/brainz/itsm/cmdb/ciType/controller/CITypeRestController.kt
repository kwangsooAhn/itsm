/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.controller

import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.itsm.cmdb.ciType.service.CITypeService
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/rest/cmdb/types")
class CITypeRestController(private val ciTypeService: CITypeService) {

    /**
     * CI Type 목록 조회
     */
    @GetMapping("/", "")
    fun getCmdbTypes(request: HttpServletRequest, model: Model): List<CmdbTypeListDto> {
        val params = LinkedMultiValueMap<String, String>()
        params["search"] = request.getParameter("search")
        return ciTypeService.getCmdbTypeList(params)
    }

    /**
     * CI Type 단일 조회
     */
    @GetMapping("/{typeId}")
    fun getCmdbType(@PathVariable typeId: String): String {
        return ciTypeService.getCmdbTypes(typeId)
    }
    /**
     * CI Type 등록
     */
    @PostMapping("")
    fun createCmdbTypes(@RequestBody cmdbTypeDto: CmdbTypeDto): String {
        return ciTypeService.createCmdbType(cmdbTypeDto)
    }

    /**
     * CI Type 수정
     */
    @PutMapping("/{typeId}")
    fun updateCmdbType(
        @RequestBody cmdbTypeDto: CmdbTypeDto,
        @PathVariable typeId: String
    ): String {
        return ciTypeService.updateCmdbType(cmdbTypeDto, typeId)
    }

    /**
     * CI Type 삭제
     */
    @DeleteMapping("/{typeId}")
    fun deleteCmdbType(@PathVariable typeId: String): String {
        return ciTypeService.deleteCmdbType(typeId)
    }
}
