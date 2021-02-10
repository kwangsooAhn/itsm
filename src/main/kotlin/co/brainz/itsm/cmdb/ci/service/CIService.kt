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
import co.brainz.cmdb.provider.dto.CIRelationDto
import co.brainz.cmdb.provider.dto.CITagDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.ci.constants.CIConstants
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class CIService(
    private val restTemplate: RestTemplateProvider,
    private val ciClassService: CIClassService,
    private val ciComponentDataRepository: CIComponentDataRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val listLinkedMapType: CollectionType =
        TypeFactory.defaultInstance().constructCollectionType(
            List::class.java,
            TypeFactory.defaultInstance().constructMapType(
                LinkedHashMap::class.java,
                String::class.java,
                Any::class.java
            )
        )

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
     * CI 컴포넌트 - CI 데이터 조회
     */
    fun getCIData(ciId: String, componentId: String, instanceId: String, modifyCIData: String): CIDetailDto {

        var ciDetailDto = CIDetailDto()
        // 전달된 데이터로 변경
        val map = mapper.readValue(modifyCIData, LinkedHashMap::class.java)
        val actionType = map["actionType"] as String
        if (actionType == CIConstants.ActionType.REGISTER.code || actionType == CIConstants.ActionType.MODIFY.code) {
            // 화면에서 전달된 데이터 세팅
            ciDetailDto.ciId = ciId
            ciDetailDto.ciNo = map["ciNo"] as String
            ciDetailDto.ciName = map["ciName"] as String
            ciDetailDto.ciIcon = map["ciIcon"] as String
            ciDetailDto.ciDesc = map["ciDesc"] as String
            ciDetailDto.ciStatus = map["ciStatus"] as String
            ciDetailDto.automatic = false
            ciDetailDto.typeId = map["typeId"] as String
            ciDetailDto.typeName = map["typeName"] as String
            ciDetailDto.classId = map["classId"] as String

            var ciClassDetail = ciClassService.getCIClass(map["classId"] as String)
            ciDetailDto.className = ciClassDetail.className

            val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
            ciDetailDto.createUserKey = aliceUserDto.userKey
            ciDetailDto.createDt = LocalDateTime.now()
            ciDetailDto.updateUserKey = aliceUserDto.userKey
            ciDetailDto.updateDt = LocalDateTime.now()

            // 임시 테이블의 CI 세부 데이터가 존재할 경우 합치기
            val ciComponentData = ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
            val tagDataList = mutableListOf<CITagDto>()
            val relationList = mutableListOf<CIRelationDto>()
            var ciClasses = ciClassService.getCIClassAttributes(map["classId"] as String)
            if (ciComponentData != null) {
                val ciComponentDataValue: Map<String, Any> =
                    mapper.readValue(ciComponentData?.values, object : TypeReference<Map<String, Any>>() {})
                // 태그
                val ciTags: List<Map<String, Any>> =
                        mapper.convertValue(ciComponentDataValue["ciTags"], listLinkedMapType)
                ciTags.forEach { tag ->
                    if (tag["id"] != null && tag["value"] != null) {
                        tagDataList.add(
                            CITagDto(
                                ciId = ciId,
                                tagId = tag["id"] as String,
                                tagName = tag["value"] as String
                            )
                        )
                    }
                }

                // TODO: 연관 관계

                // 세부 속성
                val ciAttributes: List<Map<String, Any>> =
                        mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
                for (ciClass in ciClasses) {
                    ciClass.attributes?.forEach { attributeValue ->
                        ciAttributes.forEach { attribute ->
                            if (attribute["id"] != null && attribute["value"] != null &&
                                attributeValue.attributeId == attribute["id"]) {
                                attributeValue.value = attribute["value"] as String
                                return@forEach
                            }
                        }
                    }
                }
            }
            ciDetailDto.ciTags = tagDataList
            ciDetailDto.ciRelations = relationList
            ciDetailDto.classes = ciClasses
        } else { // 삭제, 조회시 DB에 저장된 CI 데이터 조회
            ciDetailDto = getCI(ciId)
        }

        return ciDetailDto
    }

    /**
     * CI 컴포넌트 -  CI 세부 데이터 저장.
     */
    fun saveCIComponentData(ciId: String, ciComponentData: String): Boolean {
        val map = mapper.readValue(ciComponentData, LinkedHashMap::class.java)
        val componentId = map["componentId"] as String

        // 기존 CI 삭제
        val deleteCIComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(
            ciId, componentId
        )
        if (deleteCIComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(
                ciId, componentId
            )
        }
        // CI 추가
        val ciComponentEntity = CIComponentDataEntity(
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
        val ciComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(ciId, componentId)
        if (ciComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(ciId, componentId)
            return true
        }
        return false
    }
}
