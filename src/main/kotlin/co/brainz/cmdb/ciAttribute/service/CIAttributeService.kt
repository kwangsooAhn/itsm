/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.service

import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIAttributeService(
    private val ciAttributeRepository: CIAttributeRepository
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
