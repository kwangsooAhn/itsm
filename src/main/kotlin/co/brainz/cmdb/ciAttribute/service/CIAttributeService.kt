/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.entity.*
import co.brainz.cmdb.ciAttribute.repository.*
import co.brainz.cmdb.constants.*
import co.brainz.cmdb.dto.*
import co.brainz.framework.auth.repository.*
import co.brainz.framework.constants.*
import co.brainz.framework.exception.*
import co.brainz.framework.util.*
import co.brainz.itsm.cmdb.ciAttribute.dto.*
import kotlin.math.*
import org.slf4j.*
import org.springframework.stereotype.*

@Service
class CIAttributeService(
    private val ciAttributeRepository: CIAttributeRepository,
    private val aliceUserRepository: AliceUserRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Attribute 목록 조회.
     */
    fun getCIAttributes(ciAttributeSearchCondition: CIAttributeSearchCondition): CIAttributeReturnDto {
        val queryResult = ciAttributeRepository.findAttributeList(ciAttributeSearchCondition)
        return CIAttributeReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = ciAttributeRepository.count(),
                currentPageNum = ciAttributeSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
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
    fun createCIAttribute(ciAttributeDto: CIAttributeDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
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
                    attributeValue = ciAttributeDto.attributeValue,
                    searchYn = ciAttributeDto.searchYn,
                    mappingId = ciAttributeDto.mappingId,
                    createDt = ciAttributeDto.createDt,
                    createUser = ciAttributeDto.createUserKey?.let {
                        aliceUserRepository.findAliceUserEntityByUserKey(it)
                    }
                )
                ciAttributeRepository.save(attributeEntity)
            }
            else -> {
                restTemplateReturnDto.code = RestTemplateConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * CI Attribute 수정.
     */
    fun updateCIAttribute(ciAttributeDto: CIAttributeDto): RestTemplateReturnDto {
        val attributeEntity =
            ciAttributeRepository.findByAttributeId(ciAttributeDto.attributeId) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[CI Attribute Entity]"
            )
        val restTemplateReturnDto = RestTemplateReturnDto()
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
                attributeEntity.attributeValue = ciAttributeDto.attributeValue
                attributeEntity.searchYn = ciAttributeDto.searchYn
                attributeEntity.mappingId = ciAttributeDto.mappingId
                attributeEntity.updateUser = ciAttributeDto.updateUserKey?.let {
                    aliceUserRepository.findAliceUserEntityByUserKey(it)
                }
                attributeEntity.updateDt = ciAttributeDto.updateDt
                ciAttributeRepository.save(attributeEntity)
            }
            else -> {
                restTemplateReturnDto.code = RestTemplateConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * CI Attribute 삭제.
     */
    fun deleteCIAttribute(attributeId: String): Boolean {
        val attributeEntity = ciAttributeRepository.findByAttributeId(attributeId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CI Attribute Entity]"
        )
        ciAttributeRepository.deleteById(attributeEntity.attributeId)
        return true
    }

    /**
     * CI Attribute 목록 조회 (Group List 제외, 자기 자신 제외).
     */
    fun getCIAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): CIAttributeReturnDto {
        val queryResult = ciAttributeRepository.findAttributeListWithoutGroupList(
            attributeId,
            ciAttributeSearchCondition
        )
        return CIAttributeReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = ciAttributeRepository.count(),
                currentPageNum = ciAttributeSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }
}
