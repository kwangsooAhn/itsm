/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.dto.CISearchDto
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CIRepositoryCustom : AliceRepositoryCustom {
    fun findCI(ciId: String): CIsDto
    fun findCIList(ciSearchDto: CISearchDto): QueryResults<CIsDto>
    fun findDuplicateCiNo(ciNo: String): Long
    fun getLastCiByCiNo(ciNoPrefix: String): CIEntity?
}
