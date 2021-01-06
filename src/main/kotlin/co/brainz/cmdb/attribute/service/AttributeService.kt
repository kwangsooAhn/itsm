/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.service

import co.brainz.cmdb.attribute.repository.AttributeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AttributeService(
    private val attributeRepository: AttributeRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getCmdbAttributes() {
        TODO("Not yet implemented")

    }
}
