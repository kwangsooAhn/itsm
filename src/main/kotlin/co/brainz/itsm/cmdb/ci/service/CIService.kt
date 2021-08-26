/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIHistoryDto
import co.brainz.cmdb.dto.CIListReturnDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.constants.CIConstants
import co.brainz.itsm.cmdb.ci.dto.CIComponentDataDto
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
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
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciService: CIService,
    private val ciClassService: CIClassService,
    private val ciComponentDataRepository: CIComponentDataRepository,
    private val currentSessionUser: CurrentSessionUser
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
        return ciService.getCIDetail(ciId)
    }

    /**
     * CMDB CI 목록 조회
     */
    fun getCIs(ciSearchCondition: CISearchCondition?): CIListReturnDto {
        return ciService.getCIs(ciSearchCondition)
    }

    /**
     * CI 컴포넌트 - CI 데이터 조회
     */
    fun getCIData(ciId: String, componentId: String, instanceId: String, modifyCIData: String): CIDetailDto {
        var ciDetailDto = CIDetailDto(
            ciId = ciId
        )
        // 전달된 데이터로 변경
        val map = mapper.readValue(modifyCIData, LinkedHashMap::class.java)
        val actionType = map["actionType"] as String
        if (actionType == CIConstants.ActionType.REGISTER.code || actionType == CIConstants.ActionType.MODIFY.code) {
            // 화면에서 전달된 데이터 세팅
            ciDetailDto.ciNo = map["ciNo"] as String
            ciDetailDto.ciName = map["ciName"] as String
            ciDetailDto.ciIcon = map["ciIcon"] as String
            ciDetailDto.ciIconData = map["ciIconData"] as String
            ciDetailDto.ciDesc = map["ciDesc"] as String
            ciDetailDto.ciStatus = map["ciStatus"] as String
            ciDetailDto.interlink = false
            ciDetailDto.typeId = map["typeId"] as String
            ciDetailDto.typeName = map["typeName"] as String
            ciDetailDto.classId = map["classId"] as String

            val ciClassDetail = ciClassService.getCIClass(map["classId"] as String)
            ciDetailDto.className = ciClassDetail.className

            ciDetailDto.createUserKey = currentSessionUser.getUserKey()
            ciDetailDto.createDt = LocalDateTime.now()
            ciDetailDto.updateUserKey = currentSessionUser.getUserKey()
            ciDetailDto.updateDt = LocalDateTime.now()

            // 임시 테이블의 CI 세부 데이터가 존재할 경우 합치기
            val ciComponentData =
                ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
            val tagDataList = mutableListOf<AliceTagDto>()
            val relationList = mutableListOf<CIRelationDto>()
            val ciClasses = ciClassService.getCIClassAttributes(map["classId"] as String)
            if (ciComponentData != null) {
                val ciComponentDataValue: Map<String, Any> =
                    mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
                // 태그
                val tagData = mutableListOf<Map<String, String>>()
                tagData.addAll(mapper.convertValue(ciComponentDataValue["ciTags"], listLinkedMapType))
                tagData.forEach {
                    tagDataList.add(mapper.convertValue(it, AliceTagDto::class.java))
                }

                // 세부 속성
                val ciAttributes: List<Map<String, Any>> =
                    mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
                for (ciClass in ciClasses) {
                    ciClass.attributes?.forEach { attributeValue ->
                        run loop@{
                            ciAttributes.forEach { attribute ->
                                if (attribute["id"] != null && attribute["value"] != null &&
                                    attributeValue.attributeId == attribute["id"]
                                ) {
                                    attributeValue.value = attribute["value"] as String
                                    return@loop
                                }
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

    /**
     * CMDB 히스토리 조회
     */
    fun getCIHistory(ciId: String): List<CIHistoryDto> {
        return ciService.getHistory(ciId)
    }

    fun getCIRelation(ciId: String): List<CIRelationDto> {
        return ciService.getRelation(ciId)
    }

    /**
     * CI 컴포넌트 - CI 컴포넌트 세부 데이터 조회
     */
    fun getCIComponentData(ciId: String, componentId: String, instanceId: String): CIComponentDataDto? {
        val ciComponentData =
            ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
        val relationList = mutableListOf<CIRelationDto>()

        if (ciComponentData != null) {
            val ciComponentDataValue: Map<String, Any> =
                mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
            val relationData = mutableListOf<Map<String, String>>()
            relationData.addAll(mapper.convertValue(ciComponentDataValue["relatedCIData"], listLinkedMapType))
            relationData.forEach {
                relationList.add(mapper.convertValue(it, CIRelationDto::class.java))
            }
        }

        return if (ciComponentData != null) {
            CIComponentDataDto(
                ciId = ciComponentData.ciId,
                componentId = ciComponentData.componentId,
                values = ciComponentData.values,
                instanceId = ciComponentData.instanceId,
                ciRelations = relationList
            )
        } else {
            null
        }
    }
}
