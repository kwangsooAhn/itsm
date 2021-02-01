/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIComponentDataDto
import co.brainz.cmdb.provider.dto.CIDetailDto
import co.brainz.cmdb.provider.dto.CIListDto
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
     * CMDB CI 단일 조회
     */
    fun getCI(ciId: String): CIDetailDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.CI.GET_CI.url.replace(
                restTemplate.getKeyRegex(),
                ciId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(responseBody, CIDetailDto::class.java)
    }

    /**
     * CMDB CI 목록 조회
     */
    fun getCIs(params: LinkedMultiValueMap<String, String>): List<CIListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.CI.GET_CIS.url,
            parameters = params
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CIListDto::class.java)
        )
    }

    /**
     * CI 컴포넌트 -  CI 세부 데이터 저장.
     */
    fun saveCIComponentData(ciId: String, ciComponentDataDto: CIComponentDataDto): Boolean {
        val url = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.CI.PUT_CI_COMPONENT.url.replace(restTemplate.getKeyRegex(), ciId)
        )
        val responseEntity = restTemplate.update(url, ciComponentDataDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * CI 컴포넌트 - CI 세부 데이터 삭제.
     */
    fun deleteCIComponentData(params: LinkedMultiValueMap<String, String>): Boolean {
        val url = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.CI.DELETE_CI_COMPONENT.url,
                parameters = params
        )
        return restTemplate.delete(url).toString().isNotEmpty()
    }
}
