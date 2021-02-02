/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.controller

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CIDetailDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIListDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/cmdb/eg/cis")
class CIController(
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

    @PostMapping("")
    fun createCI(@RequestBody CIDto: CIDto): Boolean {
        return true
    }

    /**
     * CI 상세 조회.
     */
    @GetMapping("/{ciId}")
    fun getCI(@PathVariable ciId: String): CIDetailDto {
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
}
