/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.entity.CIAttributeEntity
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeReturnDto
import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIAttributeService(
    private val ciAttributeRepository: CIAttributeRepository,
    private val aliceUserRepository: AliceUserRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Attribute 목록 조회.
     */
    fun getCIAttributes(parameters: LinkedHashMap<String, Any>): CIAttributeReturnDto {
        var search: String? = null
        var offset: Long? = null
        var limit: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) offset = parameters["offset"].toString().toLong()
        if (parameters["limit"] != null) limit = parameters["limit"].toString().toLong()
        val searchDto = SearchDto(
            search = search,
            offset = offset,
            limit = limit
        )
        val ciAttributes = ciAttributeRepository.findAttributeList(searchDto)
        return CIAttributeReturnDto(
            data = ciAttributes.results,
            totalCount = ciAttributes.total
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
}
