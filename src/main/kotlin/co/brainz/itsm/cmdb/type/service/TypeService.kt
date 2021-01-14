/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.type.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TypeService (
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
        val result:List<CmdbTypeListDto> = mapper.readValue(
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
}
