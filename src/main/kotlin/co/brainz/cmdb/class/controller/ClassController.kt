/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.controller

import co.brainz.cmdb.`class`.service.ClassService
import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CmdbClassDto
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
class ClassController(
    private val classService: ClassService,
    private val cmdbDummyProvider: CmdbDummyProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("")
    fun getCmdbClasses(
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String
    ): List<CmdbClassDto> {
        return cmdbDummyProvider.getDummyClasses(searchValue)
    }

    @PostMapping("")
    fun createCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): Boolean {
        return true
    }

    @GetMapping("/{classId}")
    fun getCmdbClass(@PathVariable classId: String): CmdbClassDto {
        return cmdbDummyProvider.getDummyClass(classId)
    }

    @PutMapping("/{classId}")
    fun updateCmdbClass(@RequestBody cmdbClassDto: CmdbClassDto): Boolean {
        return true
    }

    @DeleteMapping("/{classId}")
    fun deleteCmdbClass(@PathVariable classId: String): Boolean {
        return true
    }
}
