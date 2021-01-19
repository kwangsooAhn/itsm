/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.controller

import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.provider.dto.CmdbClassDetailDto
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import org.slf4j.LoggerFactory
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
@RequestMapping("/rest/cmdb/eg/classes")
class CIClassController(
    private val ciClassService: CIClassService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCmdbClass(@PathVariable classId: String): CmdbClassDetailDto {
        return ciClassService.getCmdbClass(classId)
    }

    /**
     * CMDB Class 멀티 조회
     */
    @GetMapping("")
    fun getCmdbClasses(@RequestParam parameters: LinkedHashMap<String, Any>): List<CmdbClassListDto> {
        return ciClassService.getCmdbClasses(parameters)
    }

    /**
     * CMDB Class 저장
     */
    @PostMapping("")
    fun createCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): Boolean {
        return ciClassService.createCmdbClass(cmdbClassDto)
    }

    /**
     * CMDB Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): Boolean {
        return ciClassService.updateCmdbClass(cmdbClassDto)
    }

    /**
     * CMDB Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCmdbClass(@PathVariable classId: String): Boolean {
        return ciClassService.deleteCmdbClass(classId)
    }
}
