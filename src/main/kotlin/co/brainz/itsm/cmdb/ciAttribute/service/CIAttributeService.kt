/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeReturnDto
import co.brainz.framework.auth.dto.AliceUserDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Transactional
class CIAttributeService(
    private val ciAttributeService: CIAttributeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Attribute 목록 조회.
     */
    fun getCIAttributes(params: LinkedHashMap<String, Any>): CIAttributeReturnDto {
        return ciAttributeService.getCIAttributes(params)
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
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.createDt = LocalDateTime.now()
        ciAttributeDto.createUserKey = aliceUserDto.userKey
        val returnDto = ciAttributeService.createCIAttribute(ciAttributeDto)
        return returnDto.code
    }

    /**
     * Attribute 수정.
     */
    fun updateCIAttribute(attributeId: String, attributeData: String): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val ciAttributeDto = makeCIAttributeDto(attributeData)
        ciAttributeDto.updateDt = LocalDateTime.now()
        ciAttributeDto.updateUserKey = aliceUserDto.userKey
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
            mappingId = map["mappingId"] as String?,
            attributeValue = mapper.writeValueAsString(map["attributeValue"])
        )
    }
}
