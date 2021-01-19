/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import co.brainz.cmdb.provider.dto.CmdbClassDetailDto
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
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
@RequestMapping("/rest/cmdb/classes")
class CIClassRestController(private val ciClassService: CIClassService) {

    /**
     * CMDB Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCmdbClass(@PathVariable classId: String): CmdbClassDetailDto {
        return ciClassService.getCmdbClass(classId)
    }

    /**
     * CMDB Class 목록 조회
     */
    @GetMapping("/", "")
    fun getCmdbClasses(request: HttpServletRequest, model: Model): List<CmdbClassListDto> {
        val parameters = LinkedMultiValueMap<String, String>()
        parameters["search"] = request.getParameter("search")
        return ciClassService.getCmdbClasses(parameters)
    }

    /**
     * CMDB Class 등록
     */
    @PostMapping("")
    fun createCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): String {
        return ciClassService.createCmdbClass(cmdbClassDto)
    }

    /**
     * CMDB Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): String {
        return ciClassService.updateCmdbClass(cmdbClassDto)
    }

    /**
     * CMDB Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCmdbClass(@PathVariable classId: String): String {
        return ciClassService.deleteCmdbClass(classId)
    }
}
