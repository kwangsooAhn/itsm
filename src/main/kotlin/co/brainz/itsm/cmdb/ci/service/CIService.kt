/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIDetailDto
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class CIService(
        private val restTemplate: RestTemplateProvider,
        private val ciComponentDataRepository: CIComponentDataRepository
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
    fun saveCIComponentData(ciId: String, ciComponentData: String): Boolean {
        val map = mapper.readValue(ciComponentData, LinkedHashMap::class.java)
        val componentId = map["componentId"] as String

        // 기존 CI 삭제
        val deleteCIComponentEntity = ciComponentDataRepository.findByCiIdAnAndComponentId(
            ciId, componentId
        )
        if (deleteCIComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndAndComponentId(
                ciId, componentId
            )
        }
        // CI 추가
        val ciComponentEntity = CIComponentDataEntity (
            ciId = ciId,
            componentId = componentId,
            values = mapper.writeValueAsString(map["values"]),
            instanceId = map["instanceId"] as String
        )
        ciComponentDataRepository.save(ciComponentEntity)
        return true
    }

    /**
     * CI 컴포넌트 - CI 세부 데이터 삭제.
     */
    fun deleteCIComponentData(ciId: String, componentId: String): Boolean {
        val ciComponentEntity = ciComponentDataRepository.findByCiIdAnAndComponentId(ciId, componentId)
        if (ciComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndAndComponentId(ciId, componentId)
            return true
        }
        return false
    }
}
