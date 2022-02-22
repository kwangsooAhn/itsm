/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciAttribute.service

import co.brainz.api.constants.*
import co.brainz.cmdb.ciAttribute.service.*
import co.brainz.cmdb.dto.*
import co.brainz.itsm.cmdb.ciAttribute.dto.*
import co.brainz.itsm.user.service.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.time.*
import org.slf4j.*
import org.springframework.stereotype.*

@Service
class ApiCIAttributeService(
    private val ciAttributeService: CIAttributeService,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CI Attribute 목록 조회
     */
    fun getCIAttributes(params: LinkedHashMap<String, Any>): CIAttributeReturnDto {
        return ciAttributeService.getCIAttributes(CIAttributeSearchCondition(
            searchValue = params["search"].toString()
        ))
    }

    /**
     * CI Attribute 단일 목록 조회
     */
    fun getCIAttribute(attributeId: String): CIAttributeListDto {
        return ciAttributeService.getCIAttribute(attributeId)
    }

    /**
     * CI Attribute 상세 조회
     */
    fun getCIAttributeDetail(attributeId: String): CIAttributeDto {
        return ciAttributeService.getCIAttributeDetail(attributeId)
    }

    /**
     * CI Attribute 등록
     */
    fun createCIAttribute(attributeData: String): Boolean {
        val ciAttributeDto = this.makeCIAttributeDto(attributeData)
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciAttributeDto.createDt = LocalDateTime.now()
        ciAttributeDto.createUserKey = userEntity.userKey
        val returnDto = ciAttributeService.createCIAttribute(ciAttributeDto)
        return returnDto.status
    }

    /**
     * CI Attribute 수정
     */
    fun updateCIAttribute(attributeId: String, attributeData: String): Boolean {
        val ciAttributeDto = this.makeCIAttributeDto(attributeData)
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciAttributeDto.updateDt = LocalDateTime.now()
        ciAttributeDto.updateUserKey = userEntity.userKey
        val returnDto = ciAttributeService.updateCIAttribute(ciAttributeDto)
        return returnDto.status
    }

    /**
     * CI Attribute 삭제
     */
    fun deleteCIAttribute(attributeId: String): Boolean {
        return ciAttributeService.deleteCIAttribute(attributeId)
    }

    /**
     * CI Attribute 등록, 수정 데이터 셋팅
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
            attributeValue = mapper.writeValueAsString(map["attributeValue"])
        )
    }
}
