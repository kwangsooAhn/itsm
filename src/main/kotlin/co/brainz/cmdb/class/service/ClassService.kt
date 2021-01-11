/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.service

import co.brainz.cmdb.`class`.repository.ClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassService(
    private val classRepository: ClassRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
}
