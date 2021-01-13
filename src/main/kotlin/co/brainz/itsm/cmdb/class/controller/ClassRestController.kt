/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.`class`.controller

import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.itsm.cmdb.`class`.service.ClassService
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
class ClassRestController(private val classService: ClassService) {

    /**
     * CMDB Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCmdbClass(@PathVariable classId: String): CmdbClassListDto {
        return classService.getCmdbClass(classId)
    }

    /**
     * CMDB Class 목록 조회
     */
    @GetMapping("")
    fun getCmdbClasses(): String {
        return ""
    }

    /**
     * CMDB Class 등록
     */
    @PostMapping("")
    fun createCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): String {
        return classService.createCmdbClass(cmdbClassDto)
    }

    /**
     * CMDB Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCmdbClass(
        @RequestBody cmdbClassDto: CmdbClassDto,
        @PathVariable classId: String
    ): String {
        return classService.updateCmdbClass(classId, cmdbClassDto)
    }

    /**
     * CMDB Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCmdbClass(@PathVariable classId: String): String {
        return classService.deleteCmdbClass(classId)
    }
}
