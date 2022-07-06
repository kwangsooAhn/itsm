/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.entity.CIAttributeEntity
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeReturnDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIAttributeService(
    private val ciAttributeRepository: CIAttributeRepository,
    private val aliceUserRepository: AliceUserRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CI Attribute 목록 조회.
     */
    fun getCIAttributes(ciAttributeSearchCondition: CIAttributeSearchCondition): CIAttributeReturnDto {
        val pagingResult = ciAttributeRepository.findAttributeList(ciAttributeSearchCondition)
        return CIAttributeReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = ciAttributeRepository.count(),
                currentPageNum = ciAttributeSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / ciAttributeSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * CI Attribute 목록 단일 조회.
     */
    fun getCIAttribute(attributeId: String): CIAttributeListDto {
        return ciAttributeRepository.findAttribute(attributeId)
    }

    /**
     * CI Attribute 단일 상세 조회.
     */
    fun getCIAttributeDetail(attributeId: String): CIAttributeDto {
        return ciAttributeRepository.findAttributeDetail(attributeId)
    }

    /**
     * CI Attribute 등록.
     */
    fun createCIAttribute(ciAttributeDto: CIAttributeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val existCount = ciAttributeRepository.findDuplicationAttributeName(
            ciAttributeDto.attributeName,
            ciAttributeDto.attributeId
        )
        when (existCount) {
            0L -> {
                val attributeEntity = CIAttributeEntity(
                    attributeId = "",
                    attributeName = ciAttributeDto.attributeName,
                    attributeDesc = ciAttributeDto.attributeDesc,
                    attributeText = ciAttributeDto.attributeText,
                    attributeType = ciAttributeDto.attributeType,
                    attributeValue = mapper.writeValueAsString(ciAttributeDto.attributeValue),
                    searchYn = ciAttributeDto.searchYn,
                    searchWidth = ciAttributeDto.searchWidth,
                    mappingId = ciAttributeDto.mappingId,
                    createDt = ciAttributeDto.createDt,
                    createUser = ciAttributeDto.createUserKey?.let {
                        aliceUserRepository.findAliceUserEntityByUserKey(it)
                    }
                )
                ciAttributeRepository.save(attributeEntity)
            }
            else -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * CI Attribute 수정.
     */
    fun updateCIAttribute(ciAttributeDto: CIAttributeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val attributeEntity =
            ciAttributeRepository.findByAttributeId(ciAttributeDto.attributeId)
        if (attributeEntity != null) {
            val existCount = ciAttributeRepository.findDuplicationAttributeName(
                ciAttributeDto.attributeName,
                ciAttributeDto.attributeId
            )
            when (existCount) {
                0L -> {
                    attributeEntity.attributeName = ciAttributeDto.attributeName
                    attributeEntity.attributeDesc = ciAttributeDto.attributeDesc
                    attributeEntity.attributeText = ciAttributeDto.attributeText
                    attributeEntity.attributeType = ciAttributeDto.attributeType
                    attributeEntity.attributeValue = mapper.writeValueAsString(ciAttributeDto.attributeValue)
                    attributeEntity.searchYn = ciAttributeDto.searchYn
                    attributeEntity.searchWidth = ciAttributeDto.searchWidth
                    attributeEntity.mappingId = ciAttributeDto.mappingId
                    attributeEntity.updateUser = ciAttributeDto.updateUserKey?.let {
                        aliceUserRepository.findAliceUserEntityByUserKey(it)
                    }
                    attributeEntity.updateDt = ciAttributeDto.updateDt
                    ciAttributeRepository.save(attributeEntity)
                }
                else -> {
                    status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                }
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * CI Attribute 삭제.
     */
    fun deleteCIAttribute(attributeId: String): Boolean {
        var isSuccess = true
        val attributeEntity = ciAttributeRepository.findByAttributeId(attributeId)
        if (attributeEntity != null) {
            ciAttributeRepository.deleteById(attributeEntity.attributeId)
        } else {
            isSuccess = false
        }
        return isSuccess
    }

    /**
     * CI Attribute 목록 조회 (Group List 제외, 자기 자신 제외).
     */
    fun getCIAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): CIAttributeReturnDto {
        val pagingResult = ciAttributeRepository.findAttributeListWithoutGroupList(
            attributeId,
            ciAttributeSearchCondition
        )
        return CIAttributeReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = ciAttributeRepository.count(),
                currentPageNum = ciAttributeSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / ciAttributeSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }
}
