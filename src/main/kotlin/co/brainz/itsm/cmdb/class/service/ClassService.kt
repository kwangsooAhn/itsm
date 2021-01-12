/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.`class`.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.`class`.constants.ClassConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Transactional
class ClassService(
    private val restTemplate: RestTemplateProvider
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CMDB Class 단일 조회
     */
    fun getCmdbClass(classId: String): CmdbClassListDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.GET_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CmdbClassListDto::class.java)
        )
    }

    /**
     * CMDB 클래스 리스트 조회
     */
    fun getCmdbClasses(): String {
        return ""
    }

    /**
     * CMDB Class 등록
     */
    fun createCmdbClass(cmdbClassDto: CmdbClassDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbClassDto.createDt = LocalDateTime.now()
        cmdbClassDto.createUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.POST_CLASS.url
        )
        val responseBody = restTemplate.create(url, cmdbClassDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> ClassConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    /**
     * CMDB Class 수정
     */
    fun updateCmdbClass(classId: String, cmdbClassDto: CmdbClassDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbClassDto.updateDt = LocalDateTime.now()
        cmdbClassDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.PUT_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        val responseEntity = restTemplate.update(url, cmdbClassDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> ClassConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
            false -> ""
        }
    }

    /**
     * CMDB Class 삭제
     */
    fun deleteCmdbClass(classId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.DELETE_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        return when (restTemplate.delete(url).toString().isNotEmpty()) {
            true -> ClassConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }
}

