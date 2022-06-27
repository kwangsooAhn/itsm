/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition

interface CIRepositoryCustom : AliceRepositoryCustom {
    fun findCI(ciId: String): CIsDto
    fun findCIList(ciSearchCondition: CISearchCondition): PagingReturnDto
    fun findDuplicateCiNo(ciNo: String): Long
    fun getLastCiByCiNo(ciNoPrefix: String): CIEntity?
    fun countByTypeId(typeId: String): Long
    fun findCIModalList(ciSearchCondition: CISearchCondition): PagingReturnDto
}
