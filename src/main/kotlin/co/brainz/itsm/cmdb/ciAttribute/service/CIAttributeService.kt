/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.service.CIAttributeService
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeReturnDto
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

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
    fun saveCIAttribute(ciAttributeDto: CIAttributeDto): ZResponse {
        ciAttributeDto.createDt = LocalDateTime.now()
        ciAttributeDto.createUserKey = currentSessionUser.getUserKey()
        return ciAttributeService.createCIAttribute(ciAttributeDto)
    }

    /**
     * Attribute 수정.
     */
    fun updateCIAttribute(attributeId: String, ciAttributeDto: CIAttributeDto): ZResponse {
        ciAttributeDto.updateDt = LocalDateTime.now()
        ciAttributeDto.updateUserKey = currentSessionUser.getUserKey()
        return ciAttributeService.updateCIAttribute(ciAttributeDto)
    }

    /**
     * Attribute 삭제.
     */
    fun deleteCIAttribute(attributeId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (!ciAttributeService.deleteCIAttribute(attributeId)) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
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
