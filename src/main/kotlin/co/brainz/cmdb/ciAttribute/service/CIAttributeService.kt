/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.entity.CmdbAttributeEntity
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
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
     * CMDB Attribute 목록 조회.
     */
    fun getCmdbAttributes(parameters: LinkedHashMap<String, Any>): List<CmdbAttributeListDto> {
        var search = ""
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        return ciAttributeRepository.findAttributeList(search, offset).toList()
    }

    /**
     * CMDB Attribute 단일 조회.
     */
    fun getCmdbAttribute(attributeId: String): CmdbAttributeDto {
        return ciAttributeRepository.findAttribute(attributeId)
    }

    /**
     * CMDB Attribute 등록.
     */
    fun createCmdbAttribute(cmdbAttributeDto: CmdbAttributeDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
        val existCount = ciAttributeRepository.findDuplicationAttributeName(
            cmdbAttributeDto.attributeName,
            cmdbAttributeDto.attributeId
        )
        when (existCount) {
            0L -> {
                val attributeEntity = CmdbAttributeEntity(
                    attributeId = "",
                    attributeName = cmdbAttributeDto.attributeName,
                    attributeDesc = cmdbAttributeDto.attributeDesc,
                    attributeText = cmdbAttributeDto.attributeText,
                    attributeType = cmdbAttributeDto.attributeType,
                    attributeValue = cmdbAttributeDto.attributeValue,
                    createDt = cmdbAttributeDto.createDt,
                    createUser = cmdbAttributeDto.createUserKey?.let {
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
     * CMDB Attribute 수정.
     */
    fun updateCmdbAttribute(cmdbAttributeDto: CmdbAttributeDto): RestTemplateReturnDto {
        val attributeEntity =
            ciAttributeRepository.findByAttributeId(cmdbAttributeDto.attributeId) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[CMDB Attribute Entity]"
            )
        val restTemplateReturnDto = RestTemplateReturnDto()
        val existCount = ciAttributeRepository.findDuplicationAttributeName(
            cmdbAttributeDto.attributeName,
            cmdbAttributeDto.attributeId
        )
        when (existCount) {
            0L -> {
                attributeEntity.attributeName = cmdbAttributeDto.attributeName
                attributeEntity.attributeDesc = cmdbAttributeDto.attributeDesc
                attributeEntity.attributeText = cmdbAttributeDto.attributeText
                attributeEntity.attributeType = cmdbAttributeDto.attributeType
                attributeEntity.attributeValue = cmdbAttributeDto.attributeValue
                attributeEntity.updateUser = cmdbAttributeDto.updateUserKey?.let {
                    aliceUserRepository.findAliceUserEntityByUserKey(it)
                }
                attributeEntity.updateDt = cmdbAttributeDto.updateDt
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
     * CMDB Attribute 삭제.
     */
    fun deleteCmdbAttribute(attributeId: String): Boolean {
        val cmdbAttributeEntity = ciAttributeRepository.findByAttributeId(attributeId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB Attribute Entity]"
        )
        ciAttributeRepository.deleteById(cmdbAttributeEntity.attributeId)
        return true
    }
}
