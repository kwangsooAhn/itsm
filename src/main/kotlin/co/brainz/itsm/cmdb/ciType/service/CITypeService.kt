/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.ciType.constants.CITypeConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class CITypeService(
    private val restTemplate: RestTemplateProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CI Type 트리 조회
     */
    fun getCmdbTypeList(params: LinkedMultiValueMap<String, String>): List<CmdbTypeListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.GET_TYPES.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        val result: List<CmdbTypeListDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CmdbTypeListDto::class.java)
        )
        return result
    }

    /**
     * CI Type 상세 조회
     */
    fun getCmdbTypes(typeId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.GET_TYPE.url.replace(
                restTemplate.getKeyRegex(),
                typeId
            )
        )
        return restTemplate.get(url)
    }

    fun createCmdbType(cmdbTypeDto: CmdbTypeDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbTypeDto.createDt = LocalDateTime.now()
        cmdbTypeDto.createUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.POST_TYPE.url
        )
        val responseBody = restTemplate.create(url, cmdbTypeDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> CITypeConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    fun updateCmdbType(cmdbTypeDto: CmdbTypeDto, typeId: String): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbTypeDto.updateDt = LocalDateTime.now()
        cmdbTypeDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.PUT_TYPE.url.replace(
                restTemplate.getKeyRegex(),
                typeId
            )
        )
        val responseEntity = restTemplate.update(url, cmdbTypeDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> CITypeConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
            false -> ""
        }
    }

    fun deleteCmdbType(typeId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.DELETE_TYPE.url.replace(
                restTemplate.getKeyRegex(),
                typeId
            )
        )
        return when (restTemplate.delete(url).toString().isNotEmpty()) {
            true -> CITypeConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }
}
