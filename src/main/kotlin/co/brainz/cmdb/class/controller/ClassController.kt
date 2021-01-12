/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.controller

import co.brainz.cmdb.`class`.service.ClassService
import co.brainz.cmdb.provider.CmdbDummyProvider
import co.brainz.cmdb.provider.dto.CmdbClassDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/cmdb/eg/classes")
class ClassController(
    private val classService: ClassService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Class 단일 조회
     */
    @GetMapping("/{classId}")
    fun getCmdbClass(@PathVariable classId: String): CmdbClassDto {
        return classService.getCmdbClass(classId)
    }

    /**
     * CMDB Class 저장
     */
    @PostMapping("")
    fun createCmdbClass(@RequestBody jsonData: Any): Boolean {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return classService.createCmdbClass(mapper.convertValue(jsonData, CmdbClassDto::class.java))
    }

    /**
     * CMDB Class 수정
     */
    @PutMapping("/{classId}")
    fun updateCmdbClass(
        @PathVariable classId: String,
        @RequestBody cmdbClassDto: CmdbClassDto
    ): Boolean {
        return classService.updateCmdbClass(classId, cmdbClassDto)
    }

    /**
     * CMDB Class 삭제
     */
    @DeleteMapping("/{classId}")
    fun deleteCmdbClass(@PathVariable classId: String): Boolean {
        return classService.deleteCmdbClass(classId)
    }
}
