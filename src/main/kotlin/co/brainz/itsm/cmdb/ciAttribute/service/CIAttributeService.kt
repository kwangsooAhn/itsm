/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import co.brainz.framework.auth.dto.AliceUserDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
@Transactional
class CIAttributeService(
    private val restTemplate: RestTemplateProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Attribute 목록 조회.
     */
    fun getCIAttributes(params: LinkedMultiValueMap<String, String>): List<CIAttributeListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.GET_ATTRIBUTES.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CIAttributeListDto::class.java)
        )
    }

    /**
     * 단일 Attribute 조회.
     */
    fun getCIAttribute(attributeId: String): CIAttributeDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.GET_ATTRIBUTE.url.replace(
                restTemplate.getKeyRegex(),
                attributeId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(responseBody, CIAttributeDto::class.java)
    }

    /**
     * Attribute 등록.
     */
    fun saveCIAttribute(attributeData: String): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.createDt = LocalDateTime.now()
        ciAttributeDto.createUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Attribute.POST_ATTRIBUTE.url)
        val responseBody = restTemplate.create(url, ciAttributeDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> {
                val restTemplateReturnDto =
                    mapper.readValue(responseBody.body.toString(), RestTemplateReturnDto::class.java)
                restTemplateReturnDto.code
            }
            false -> ""
        }
    }

    /**
     * Attribute 수정.
     */
    fun updateCIAttribute(attributeId: String, attributeData: String): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.updateDt = LocalDateTime.now()
        ciAttributeDto.updateUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.PUT_ATTRIBUTE.url.replace(
                restTemplate.getKeyRegex(),
                attributeId
            )
        )
        val responseBody = restTemplate.update(url, ciAttributeDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> {
                val restTemplateReturnDto =
                    mapper.readValue(responseBody.body.toString(), RestTemplateReturnDto::class.java)
                restTemplateReturnDto.code
            }
            false -> ""
        }
    }

    /**
     * Attribute 데이터 파싱.
     */
    private fun makeCIAttributeDto(attributeData: String): CIAttributeDto {
        val map = mapper.readValue(attributeData, LinkedHashMap::class.java)
        return CIAttributeDto(
            attributeId = map["attributeId"] as String,
            attributeName = map["attributeName"] as String,
            attributeType = map["attributeType"] as String,
            attributeText = map["attributeText"] as String,
            attributeDesc = map["attributeDesc"] as String,
            attributeValue = mapper.writeValueAsString(map["attributeValue"])
        )
    }

    /**
     * Attribute 삭제.
     */
    fun deleteCIAttribute(attributeId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.DELETE_ATTRIBUTE.url.replace(
                restTemplate.getKeyRegex(),
                attributeId
            )
        )
        var returnValue = "-1"
        val responseEntity = restTemplate.delete(url)
        if (responseEntity.body.toString().toBoolean()) {
            returnValue = "0"
        }
        return returnValue
    }
}
