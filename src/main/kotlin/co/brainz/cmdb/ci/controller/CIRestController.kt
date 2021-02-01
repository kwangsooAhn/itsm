/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.controller

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CIComponentDataDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
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
@RequestMapping("/rest/cmdb/eg/cis")
class CIRestController(
    private val ciService: CIService,
    private val cmdbDummyProvider: CmdbDummyProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI 목록 조회.
     */
    @GetMapping("")
    fun getCIs(@RequestParam parameters: LinkedHashMap<String, Any>): List<CIListDto> {
        return ciService.getCIs(parameters)
    }

    /**
     * CI 등록.
     * CI 등록은 실제 상세 속성 데이터와 함께 이루어짐.
     */
    @PostMapping("")
    fun createCI(@RequestBody ciDto: CIDto): RestTemplateReturnDto {
        return ciService.createCI(ciDto)
    }

    /**
     * CI 수정.
     */
    @PutMapping("/{ciId}")
    fun updateCI(@PathVariable ciId: String, @RequestBody ciDto: CIDto): RestTemplateReturnDto {
        return ciService.updateCI(ciId, ciDto)
    }

    @DeleteMapping("/{ciId}")
    fun deleteCI(): Boolean {
        return true
    }

    @PutMapping("/{ciId}/data")
    fun saveCIComponentData(@PathVariable ciId: String, @RequestBody ciComponentDataDto: CIComponentDataDto): Boolean {
        return ciService.saveCIComponentData(ciComponentDataDto)
    }

    @DeleteMapping("/data")
    fun deleteCIComponentData(
        @RequestParam(value = "ciId") ciId: String,
        @RequestParam(value = "componentId") componentId: String
    ): Boolean {
        return ciService.deleteCIComponentData(ciId, componentId)
    }
}
