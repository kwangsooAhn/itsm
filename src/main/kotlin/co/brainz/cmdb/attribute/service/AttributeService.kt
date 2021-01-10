/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.service

import co.brainz.cmdb.attribute.repository.AttributeRepository
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AttributeService(
    private val attributeRepository: AttributeRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getCmdbAttributes(parameters: LinkedHashMap<String, Any>): List<CmdbAttributeListDto> {
        var search = ""
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        return attributeRepository.findAttributeList(search, offset).toList()
    }
}
