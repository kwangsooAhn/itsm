/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciRelation.repository

import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIRelationRepositoryCustom : AliceRepositoryCustom {
    fun selectByCiId(ciId: String): List<CIRelationDto>
}
