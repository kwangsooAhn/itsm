/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
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
    fun getAttributes(params: LinkedMultiValueMap<String, String>): List<CmdbAttributeListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.GET_ATTRIBUTES.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CmdbAttributeListDto::class.java)
        )
    }

    /**
     * 단일 Attribute 조회.
     */
    fun getAttribute(attributeId: String): CmdbAttributeDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Attribute.GET_ATTRIBUTE.url.replace(
                restTemplate.getKeyRegex(),
                attributeId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(responseBody, CmdbAttributeDto::class.java)
    }

    /**
     * Attribute 등록 / 수정.
     */
    fun saveAttribute(cmdbAttributeDto: CmdbAttributeDto): String {
        val a = ""
        return "0"
    }

    /**
     * Attribute 삭제.
     */
    fun deleteAttribute(attributeId: String): String {
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
