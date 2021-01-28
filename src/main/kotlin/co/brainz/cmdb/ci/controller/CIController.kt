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
class CIController(
    private val ciService: CIService,
    private val cmdbDummyProvider: CmdbDummyProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("")
    fun getCIs(
        @RequestParam(value = "search", defaultValue = "") search: String
    ): List<CIListDto> {
        return cmdbDummyProvider.getDummyCis(search)
    }

    @PostMapping("")
    fun createCI(@RequestBody CIDto: CIDto): Boolean {
        return true
    }

    @GetMapping("/{ciId}")
    fun getCI(@PathVariable ciId: String): CIDto {
        return cmdbDummyProvider.getDummyCi(ciId)
    }

    @PutMapping("/{ciId}")
    fun updateCI(@RequestBody CIDto: CIDto): Boolean {
        return true
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
