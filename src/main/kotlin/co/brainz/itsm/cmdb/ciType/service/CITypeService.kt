/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CITypeDto
import co.brainz.cmdb.provider.dto.CITypeListDto
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
    fun getCITypeList(params: LinkedMultiValueMap<String, String>): List<CITypeListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.GET_TYPES.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        val result: List<CITypeListDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CITypeListDto::class.java)
        )
        return result
    }

    /**
     * CI Type 상세 조회
     */
    fun getCITypes(typeId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.GET_TYPE.url.replace(
                restTemplate.getKeyRegex(),
                typeId
            )
        )
        return restTemplate.get(url)
    }

    fun createCIType(ciTypeDto: CITypeDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciTypeDto.createDt = LocalDateTime.now()
        ciTypeDto.createUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.POST_TYPE.url
        )
        val responseBody = restTemplate.create(url, ciTypeDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> CITypeConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    fun updateCIType(ciTypeDto: CITypeDto, typeId: String): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciTypeDto.updateDt = LocalDateTime.now()
        ciTypeDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Type.PUT_TYPE.url.replace(
                restTemplate.getKeyRegex(),
                typeId
            )
        )
        val responseEntity = restTemplate.update(url, ciTypeDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> CITypeConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
            false -> ""
        }
    }

    fun deleteCIType(typeId: String): String {
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
