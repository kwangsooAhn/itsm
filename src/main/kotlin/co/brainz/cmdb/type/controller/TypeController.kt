/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.controller

import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.type.service.TypeService
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
@RequestMapping("/rest/cmdb/eg/types")
class TypeController(
    private val typeService: TypeService,
    private val cmdbDummyProvider: CmdbDummyProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("")
    fun getCmdbTypes(
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String
    ): List<CmdbTypeDto> {
        return cmdbDummyProvider.getDummyTypes(searchValue)
    }

    @PostMapping("")
    fun createCmdbType(@RequestBody cmdbTypeDto: CmdbTypeDto): Boolean {
        return true
    }

    @GetMapping("/{typeId}")
    fun getCmdbType(@PathVariable typeId: String): CmdbTypeDto {
        return cmdbDummyProvider.getDummyType(typeId)
    }

    @PutMapping("/{typeId}")
    fun updateCmdbType(@RequestBody cmdbTypeDto: CmdbTypeDto): Boolean {
        return true
    }

    @DeleteMapping("/{typeId}")
    fun deleteCmdbType(@PathVariable typeId: String): Boolean {
        return true
    }
}
