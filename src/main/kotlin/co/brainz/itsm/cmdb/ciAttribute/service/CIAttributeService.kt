/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.dto.*
import co.brainz.framework.util.*
import co.brainz.itsm.cmdb.ciAttribute.dto.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.time.*
import javax.transaction.*
import org.slf4j.*
import org.springframework.stereotype.*

@Service
@Transactional
class CIAttributeService(
    private val ciAttributeService: CIAttributeService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Attribute 목록 조회.
     */
    fun getCIAttributes(ciAttributeSearchCondition: CIAttributeSearchCondition): CIAttributeReturnDto {
        return ciAttributeService.getCIAttributes(ciAttributeSearchCondition)
    }

    /**
     * Attribute 상세 조회.
     */
    fun getCIAttribute(attributeId: String): CIAttributeDto {
        return ciAttributeService.getCIAttributeDetail(attributeId)
    }

    /**
     * Attribute 등록.
     */
    fun saveCIAttribute(attributeData: String): String {
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.createDt = LocalDateTime.now()
        ciAttributeDto.createUserKey = currentSessionUser.getUserKey()
        val returnDto = ciAttributeService.createCIAttribute(ciAttributeDto)
        return returnDto.code
    }

    /**
     * Attribute 수정.
     */
    fun updateCIAttribute(attributeId: String, attributeData: String): String {
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.updateDt = LocalDateTime.now()
        ciAttributeDto.updateUserKey = currentSessionUser.getUserKey()
        val returnDto = ciAttributeService.updateCIAttribute(ciAttributeDto)
        return returnDto.code
    }

    /**
     * Attribute 삭제.
     */
    fun deleteCIAttribute(attributeId: String): String {
        var returnValue = "-1"
        if (ciAttributeService.deleteCIAttribute(attributeId)) {
            returnValue = "0"
        }
        return returnValue
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
            searchYn = map["searchYn"] as Boolean,
            mappingId = map["mappingId"] as String?,
            attributeValue = mapper.writeValueAsString(map["attributeValue"])
        )
    }

    /**
     * Attribute 목록 조회 (Group List 제외, 자기 자신 제외).
     */
    fun getCIAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): CIAttributeReturnDto {
        return ciAttributeService.getCIAttributeListWithoutGroupList(attributeId, ciAttributeSearchCondition)
    }
}
