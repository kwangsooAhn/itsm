/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.controller

import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassListDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

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

    /**
     * CMDB CI Class에 따른 세부 속성 조회
     */
    @GetMapping("/{classId}/attributes")
    fun getCIClassAttributes(@PathVariable classId: String): MutableList<CIClassDetailValueDto> {
        return ciClassService.getCIClassAttributes(classId)
    }
}
