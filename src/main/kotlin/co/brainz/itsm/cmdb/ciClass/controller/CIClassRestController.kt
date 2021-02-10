/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.controller

import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassListDto
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
     * CMDB CI Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCIClass(@PathVariable classId: String): CIClassDetailDto {
        return ciClassService.getCIClass(classId)
    }

    /**
     * CMDB CI Class 목록 조회
     */
    @GetMapping("/", "")
    fun getCIClasses(request: HttpServletRequest, model: Model): List<CIClassListDto> {
        val parameters = LinkedMultiValueMap<String, String>()
        parameters["search"] = request.getParameter("search")
        return ciClassService.getCIClasses(parameters)
    }

    /**
     * CMDB CI Class 등록
     */
    @PostMapping("")
    fun createCIClass(@RequestBody CIClassDto: CIClassDto): String {
        return ciClassService.createCIClass(CIClassDto)
    }

    /**
     * CMDB CI Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCIClass(@RequestBody CIClassDto: CIClassDto): String {
        return ciClassService.updateCIClass(CIClassDto)
    }

    /**
     * CMDB CI Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCIClass(@PathVariable classId: String): String {
        return ciClassService.deleteCIClass(classId)
    }

    /**
     * CMDB CI Class에 따른 세부 속성 조회
     */
    @GetMapping("/{classId}/attributes")
    fun getCIClassAttributes(@PathVariable classId: String): MutableList<CIClassDetailValueDto> {
        return ciClassService.getCIClassAttributes(classId)
    }
}
