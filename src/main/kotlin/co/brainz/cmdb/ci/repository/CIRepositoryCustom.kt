/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.provider.dto.CIsDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIRepositoryCustom : AliceRepositoryCustom {
    fun findCIList(search: String, offset: Long?, tags: List<String>): List<CIsDto>
    fun findDuplicateCiNo(ciNo: String): Long
}
