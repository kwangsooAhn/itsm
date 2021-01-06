/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.repository.CiRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CiService(
    private val ciRepository: CiRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


}
