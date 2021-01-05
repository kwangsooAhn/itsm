/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.service

import co.brainz.cmdb.type.repository.TypeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TypeService(
    private val typeRepository: TypeRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


}
