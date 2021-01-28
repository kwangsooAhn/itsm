/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.controller

import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassListDto
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
     * CMDB CI Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCIClass(@PathVariable classId: String): CIClassDetailDto {
        return ciClassService.getCIClass(classId)
    }

    /**
     * CMDB CI Class 멀티 조회
     */
    @GetMapping("")
    fun getCIClasses(@RequestParam parameters: LinkedHashMap<String, Any>): List<CIClassListDto> {
        return ciClassService.getCIClasses(parameters)
    }

    /**
     * CMDB CI Class 저장
     */
    @PostMapping("")
    fun createCIClass(@RequestBody CIClassDto: CIClassDto): Boolean {
        return ciClassService.createCIClass(CIClassDto)
    }

    /**
     * CMDB CI Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCIClass(@RequestBody CIClassDto: CIClassDto): Boolean {
        return ciClassService.updateCIClass(CIClassDto)
    }

    /**
     * CMDB CI Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCIClass(@PathVariable classId: String): Boolean {
        return ciClassService.deleteCIClass(classId)
    }
}
