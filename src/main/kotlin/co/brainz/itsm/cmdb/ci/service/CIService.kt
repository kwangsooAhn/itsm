/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbCiDto
import co.brainz.cmdb.provider.dto.CmdbCiListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class CIService(
    private val restTemplate: RestTemplateProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CMDB Ci 단일 조회
     */
    fun getCi(ciId: String): CmdbCiDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Ci.GET_CI.url.replace(
                restTemplate.getKeyRegex(),
                ciId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(responseBody, CmdbCiDto::class.java)
    }

    /**
     * CMDB Ci 목록 조회
     */
    fun getCis(params: LinkedMultiValueMap<String, String>): List<CmdbCiListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Ci.GET_CIS.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CmdbCiListDto::class.java)
        )
    }
}
